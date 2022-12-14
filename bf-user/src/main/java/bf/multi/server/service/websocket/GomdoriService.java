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
import bf.multi.server.service.GeoService;
import bf.multi.server.service.RequestsService;
import bf.multi.server.service.UserService;
import bf.multi.server.service.firebase.FCMService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GomdoriService {
    private final JwtTokenProvider jwtTokenProvider;
    private final FindConnectedUsersService findConnectedUsersService;
    private final FCMService fcmService;
    private final UserService userService;
    private final GeoService geoService;
    private final RequestsService requestsService;
    private final UserRepository userRepository;
    private final HelpeeRepository helpeeRepository;
    private final HelperRepository helperRepository;
    private final RequestsRepository requestsRepository;
    private final HelpsRepository helpsRepository;
    private final SimpMessageSendingOperations simpMessageSendingOperations;


    // ?????? ?????? ?????? ?????? ??????
    @SneakyThrows
    public void sendMessage(MessageDto messageDto){ // ????????? ????????? ?????? ?????? ??????
        Timestamp now = new Timestamp(System.currentTimeMillis());
        messageDto.setTime(now);
        if(MessageDto.MessageType.ENTER.equals(messageDto.getType())){ // ?????? ?????? ????????? ???
            // helps ?????? + ?????? ????????? ??????
            createHelp(messageDto); // help default ??????
            messageDto.setHelpRequest(null);
            simpMessageSendingOperations.convertAndSend("/map/"+ messageDto.getSub(), messageDto);
        }else if(MessageDto.MessageType.QUIT.equals(messageDto.getType())){ // ?????? ?????? ?????? ??? ??? ???
            // TODO: ???????????????????
        }else if(MessageDto.MessageType.HELP.equals(messageDto.getType())) { // ?????? ?????? ??? ???
            // ???????????? ?????? ??? + ?????? ?????? ????????? ??????
            createRequests(messageDto); // request default ??????
            // ???:??? ????????? FCM ????????? ?????????
            Optional<User> helpeeUser = userRepository.findByUsername(jwtTokenProvider.getUsernameByToken(messageDto.getHelpRequest().getHelpeeJwt()));
            List<Helps> helpsList = helpsRepository.findAllBySuccessIsFalse();
            List<User> userList = new ArrayList<>();
            helpsList.forEach(list -> {
                userList.add(userRepository.findByUsername(jwtTokenProvider.getUsernameByToken(list.getHelpsJwt())).get());
            });
            userList.forEach(list -> {
                try {
                    fcmService.sendMessageTo(
                            list.getFCMToken(),
                            "[ " + helpeeUser.get().getUsername() + " ] ?????? ????????? ??????????????????!!",
                            "<??????????????????> " + messageDto.getHelpRequest().getRequestDetail());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            // ?????? ????????? ?????? ?????????
            simpMessageSendingOperations.convertAndSend("/map/" + messageDto.getSub(), messageDto);
        }else if(MessageDto.MessageType.ACCEPT.equals(messageDto.getType())) { // ?????? ?????? ??? ???
            // FCM ????????? ??????
            Optional<User> helpeeUser = userRepository.findByUsername(jwtTokenProvider.getUsernameByToken(messageDto.getHelpRequest().getHelpeeJwt()));
            Optional<User> helperUser = userRepository.findByUsername(jwtTokenProvider.getUsernameByToken(messageDto.getJwt()));
            fcmService.sendMessageTo(
                    helpeeUser.get().getFCMToken(),
                    "[ " + helpeeUser.get().getUsername() + " ] ?????? ?????? ????????? ?????????????????????!!",
                    "[ " + helperUser.get().getUsername() + " ] ?????? ?????? ????????? ???????????????");
            // STOMP ????????? ??????
            List<MessageDto> messageDtoList = findConnectedUsersService.deleteAcceptPings(
                    messageDto.getJwt(),
                    messageDto.getHelpRequest().getHelpeeJwt());
            messageDtoList.forEach(list -> {
                simpMessageSendingOperations.convertAndSend("/map/main", list);
            });
            simpMessageSendingOperations.convertAndSendToUser(
                    jwtTokenProvider.getUsernameByToken(messageDto.getHelpRequest().getHelpeeJwt()),
                    "/map/" + messageDto.getSub(),
                    messageDto);
        }
    }

    // ????????? ???????????? helps ?????? ?????? ??????
    @Transactional
    public void createHelp(MessageDto messageDto){
        Optional<Helper> helper = helperRepository.findHelperByUser_Username(jwtTokenProvider.getUsernameByToken(messageDto.getJwt()));
        Helps helps = Helps.builder()
                .helper(helper.get()).requests(null).helpsJwt(messageDto.getJwt())
                .x(messageDto.getLocation().getX()).y(messageDto.getLocation().getY())
                .acceptTime(messageDto.getTime()).finishTime(null)
                .success(false)
                .helperRate(2.5).helpeeRate(2.5)
                .helperMessage(null).helpeeMessage(null)
                .build();
        helpsRepository.save(helps);
    }
    // ???:?????? ?????? ???????????? ???

    // ???????????? ?????? ????????? ??? ????????? ?????? ?????? ??????
    @Transactional
    public void createRequests(MessageDto messageDto) {
        Helpee helpee = helpeeRepository.findByUser_Username(
                jwtTokenProvider.getUsernameByToken(messageDto.getHelpRequest().getHelpeeJwt()));
        Requests requests = Requests.builder()
                .helpee(helpee).complete(false).requestsJwt(messageDto.getJwt())
                .requestType(messageDto.getHelpRequest().getRequestType())
                .requestDetail(messageDto.getHelpRequest().getRequestDetail())
                .location(geoService.reverseGeocoding(messageDto.getLocation().getY(), messageDto.getLocation().getX()))
                .x(messageDto.getLocation().getX()).y(messageDto.getLocation().getY())
                .helpee(helpee)
                .requestTime(messageDto.getTime())
                .build();
        requestsRepository.save(requests);
    }

    public HelpeePingDto responseHelpeePing(HttpServletRequest httpServletRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.loadUserByEncodedEmail(userDetails.getPassword());
        Helpee helpee = userService.loadHelpeeByEncodedEmail(userDetails.getPassword());
        Requests requests = requestsService.loadRecentByHelpee(helpee);
        try {
            return HelpeePingDto.builder()
                    .name(user.getUsername())
                    .photoLink(user.getPhotoLink())
                    .type(helpee.getType())
                    .age(user.getAge())
                    .gender(user.getGender())
                    .phone(user.getPhone())
                    .location(geoService.reverseGeocoding(requests.getY(), requests.getX()))
                    .helpRequestDto(HelpRequestDto.builder()
                            .helpeeJwt(requests.getRequestsJwt())
                            .detailLocation(geoService.reverseGeocoding(requests.getY(), requests.getX()))
                            .requestDetail(requests.getRequestDetail())
                            .requestType(requests.getRequestType())
                            .build())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public HelperPingDto responseHelperPing() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.loadUserByEncodedEmail(userDetails.getPassword());
        try {
            Helper helper = userService.loadHelperByEncodedEmail(userDetails.getPassword());
            Helps helps = helpsRepository.findDistinctTopBySuccessIsFalseAndHelper_User_UsernameOrderByAcceptTimeDesc(user.getUsername());
            String location = geoService.reverseGeocoding(helps.getY(), helps.getX());
            return new HelperPingDto(user, helper, location);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public MatchingDataDto generateMatchingData(String token1, String token2) {
        try {
            Optional<User> user1 = userRepository.findByUsername(jwtTokenProvider.getUsernameByToken(token1));
            Optional<User> user2 = userRepository.findByUsername(jwtTokenProvider.getUsernameByToken(token2));
            if (helperRepository.findHelperByUser_Email(user1.get().getEmail()).isPresent()) {
                Optional<Helper> helper = helperRepository.findHelperByUser_Email(user1.get().getEmail());
                Helps helps = helpsRepository.findDistinctFirstBySuccessIsTrueAndHelperOrderByAcceptTimeDesc(helper);
                Helpee helpee = helpeeRepository.findByUser_Username(user2.get().getUsername());
                Requests requests = requestsRepository.findDistinctTopByCompleteIsTrueAndHelpeeOrderByRequestTimeDesc(helpee);
                return MatchingDataDto.builder()
                        .helperLocation(MatchingDataDto.HelperLocation.builder().helperX(helps.getX()).helperY(helps.getY()).build())
                        .helpeeLocation(MatchingDataDto.HelpeeLocation.builder().helpeeX(requests.getX()).helpeeY(requests.getY()).build())
                        .build();
            }
            Optional<Helper> helper = helperRepository.findHelperByUser_Email(user2.get().getEmail());
            Helps helps = helpsRepository.findDistinctFirstBySuccessIsTrueAndHelperOrderByAcceptTimeDesc(helper);
            Helpee helpee = helpeeRepository.findByUser_Username(user1.get().getUsername());
            Requests requests = requestsRepository.findDistinctTopByCompleteIsTrueAndHelpeeOrderByRequestTimeDesc(helpee);
            return MatchingDataDto.builder()
                    .helperLocation(MatchingDataDto.HelperLocation.builder().helperX(helps.getX()).helperY(helps.getY()).build())
                    .helpeeLocation(MatchingDataDto.HelpeeLocation.builder().helpeeX(requests.getX()).helpeeY(requests.getY()).build())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    // accept Message ?????? ??? helps, requests ????????????
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
