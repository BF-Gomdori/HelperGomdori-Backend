package bf.multi.server.service.websocket;

import bf.multi.server.domain.dto.websocket.*;
import bf.multi.server.domain.helpee.Helpee;
import bf.multi.server.domain.helpee.HelpeeRepository;
import bf.multi.server.domain.helper.Helper;
import bf.multi.server.domain.helper.HelperRepository;
import bf.multi.server.domain.helps.Helps;
import bf.multi.server.domain.helps.HelpsRepository;
import bf.multi.server.domain.requests.Requests;
import bf.multi.server.domain.requests.RequestsRepository;
import bf.multi.server.domain.user.User;
import bf.multi.server.domain.user.UserRepository;
import bf.multi.server.security.JwtTokenProvider;
import bf.multi.server.service.RequestsService;
import bf.multi.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GomdoriService {
    private final JwtTokenProvider jwtTokenProvider;
    private final FindConnectedUsersService findConnectedUsersService;
    private final UserService userService;
    private final RequestsService requestsService;
    private final UserRepository userRepository;
    private final HelpeeRepository helpeeRepository;
    private final HelperRepository helperRepository;
    private final RequestsRepository requestsRepository;
    private final HelpsRepository helpsRepository;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    // 현재 자기 위치 정보 전송
    public void sendMessage(MessageDto messageDto){ // 메세지 타입에 따라 동작 구분
        Timestamp now = new Timestamp(System.currentTimeMillis());
        messageDto.setTime(now);
        if(MessageDto.MessageType.ENTER.equals(messageDto.getType())){ // 위치 정보 표시할 때
            // helps 생성 + 입장 메세지 전송
            createHelp(messageDto); // help default 생성
            messageDto.setHelpRequest(null);
            simpMessageSendingOperations.convertAndSend("/map/"+ messageDto.getSub(), messageDto);
        }else if(MessageDto.MessageType.QUIT.equals(messageDto.getType())){ // 위치 정보 표시 안 할 때
            // TODO: 뭐해줘야되지?
        }else if(MessageDto.MessageType.HELP.equals(messageDto.getType())){ // 도움 요청 할 때
            // 요청사항 저장 및 + 도움 요청 메세지 전송
            createRequests(messageDto); // request default 생성
            // 메인 화면에 정보 뿌리기
            simpMessageSendingOperations.convertAndSend("/map/"+ messageDto.getSub(), messageDto);
        }else if(MessageDto.MessageType.ACCEPT.equals(messageDto.getType())){ // 도움 수락 할 때
            // convertAndSendToUser 로도 동작할 수 있긴함
            simpMessageSendingOperations.convertAndSend("/map/"+ messageDto.getSub(), messageDto);
        }
    }

    // 베프가 들어오면 helps 객체 하나 생성
    @Transactional
    public void createHelp(MessageDto messageDto){
        Helper helper = helperRepository.findHelperByUser_Username(jwtTokenProvider.getUsernameByToken(messageDto.getJwt()));
        Helps helps = Helps.builder()
                .helper(helper).requests(null)
                .x(messageDto.getLocation().getX()).y(messageDto.getLocation().getY())
                .acceptTime(messageDto.getTime()).finishTime(null)
                .success(false)
                .helperRate(2.5).helpeeRate(2.5)
                .helperMessage(null).helpeeMessage(null)
                .build();
        helpsRepository.save(helps);
    }
    // 베:프가 도움 수락했을 때

    // 곰돌이가 도움 요청할 때 채팅방 하나 따로 생성
    @Transactional
    public void createRequests(MessageDto messageDto) {
        Helpee helpee = helpeeRepository.findByUser_Username(
                jwtTokenProvider.getUsernameByToken(messageDto.getHelpRequest().getHelpeeJwt()));
        Requests requests = Requests.builder()
                .helpee(helpee).complete(false)
                .requestType(messageDto.getHelpRequest().getRequestType())
                .requestDetail(messageDto.getHelpRequest().getRequestDetail())
                .location(messageDto.getHelpRequest().getDetailLocation())
                .x(messageDto.getLocation().getX()).y(messageDto.getLocation().getY())
                .helpee(helpee)
                .requestTime(messageDto.getTime())
                .build();
        requestsRepository.save(requests);
    }

    public HelpeePingDto responseHelpeePing(HttpServletRequest httpServletRequest){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.loadUserByEncodedEmail(userDetails.getPassword());
        Helpee helpee = userService.loadHelpeeByEncodedEmail(userDetails.getPassword());
        Requests requests = requestsService.loadRecentByHelpee(helpee);
        return HelpeePingDto.builder()
                .name(user.getUsername())
                .photoLink(user.getPhotoLink())
                .type(helpee.getType())
                .age(user.getAge())
                .gender(user.getGender())
                .phone(user.getPhone())
                .location(requests.getLocation())
                .helpRequestDto(HelpRequestDto.builder()
                        .helpeeJwt(String.valueOf(httpServletRequest.getHeader("Authorization").startsWith("Bearer ")))
                        .detailLocation(requests.getLocation())
                        .requestDetail(requests.getRequestDetail())
                        .requestType(requests.getRequestType())
                        .build())
                .build();
    }

    public HelperPingDto responseHelperPing(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.loadUserByEncodedEmail(userDetails.getPassword());
        Helper helper = userService.loadHelperByEncodedEmail(userDetails.getPassword());
        return new HelperPingDto(user, helper);
    }

    public MatchingDataDto generateMatchingData(String token1, String token2){
        Optional<User> user1 = userRepository.findByUsername(jwtTokenProvider.getUsernameByToken(token1));
        Optional<User> user2 = userRepository.findByUsername(jwtTokenProvider.getUsernameByToken(token2));
        if(helperRepository.findHelperByUser_Email(user1.get().getEmail()).isPresent()){
            Optional<Helper> helper = helperRepository.findHelperByUser_Email(user1.get().getEmail());
            Helps helps = helpsRepository.findDistinctFirstByHelperOrderByAcceptTimeDesc(helper);
            Helpee helpee = helpeeRepository.findByUser_Username(user2.get().getUsername());
            Requests requests = requestsRepository.findDistinctTopByHelpeeOrderByRequestTimeDesc(helpee);
            return MatchingDataDto.builder()
                    .helperLocation(MatchingDataDto.HelperLocation.builder().helperX(helps.getX()).helperY(helps.getY()).build())
                    .helpeeLocation(MatchingDataDto.HelpeeLocation.builder().helpeeX(requests.getX()).helpeeY(requests.getY()).build())
                    .build();
        }
        Optional<Helper> helper = helperRepository.findHelperByUser_Email(user2.get().getEmail());
        Helps helps = helpsRepository.findDistinctFirstByHelperOrderByAcceptTimeDesc(helper);
        Helpee helpee = helpeeRepository.findByUser_Username(user1.get().getUsername());
        Requests requests = requestsRepository.findDistinctTopByHelpeeOrderByRequestTimeDesc(helpee);
        return MatchingDataDto.builder()
                .helperLocation(MatchingDataDto.HelperLocation.builder().helperX(helps.getX()).helperY(helps.getY()).build())
                .helpeeLocation(MatchingDataDto.HelpeeLocation.builder().helpeeX(requests.getX()).helpeeY(requests.getY()).build())
                .build();
    }

    // accept Message 왔을 때 helps, requests 업데이트
    @Transactional
    public void updateHelpsAndRequests(MessageDto messageDto){
        Optional<User> helperUser = userRepository.findByUsername(
                jwtTokenProvider.getUsernameByToken(messageDto.getJwt()));
        Optional<User> helpeeUser = userRepository.findByUsername(
                jwtTokenProvider.getUsernameByToken(messageDto.getHelpRequest().getHelpeeJwt()));
        Optional<Helper> helper = helperRepository.findHelperByUser_Email(helperUser.get().getEmail());
        Helpee helpee = helpeeRepository.findByUser_Username(helpeeUser.get().getUsername());

        Requests requests = requestsRepository.findDistinctTopByHelpeeOrderByRequestTimeDesc(helpee);
        Helps helps = helpsRepository.findDistinctFirstByHelperOrderByAcceptTimeDesc(helper);
        requests.updateRequests(messageDto);
        helps.updateHelps(messageDto, requests);
    }

}
