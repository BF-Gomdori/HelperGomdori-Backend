package bf.multi.server.websocket.controller;

import bf.multi.server.domain.requests.Requests;
import bf.multi.server.domain.user.User;
import bf.multi.server.domain.user.UserRepository;
import bf.multi.server.security.JwtTokenProvider;
import bf.multi.server.service.UserService;
import bf.multi.server.websocket.domain.MessageDto;
import bf.multi.server.websocket.service.GomdoriService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@CrossOrigin
public class WebSocketController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final GomdoriService gomdoriService;
    private final JwtTokenProvider jwtTokenProvider;

    // 자신의 위치 정보 표시한다고 눌렀을 때
    @MessageMapping("/enter")
    public void initEnter(MessageDto messageDto) {
        String username = jwtTokenProvider.getUsernameByToken(messageDto.getJwt());
        Optional<User> user = userRepository.findByUsername(username);
        if(messageDto.getType().equals(MessageDto.MessageType.ENTER)) {
            log.info("============= 베:프 현재 위치 전송 =============");
            log.info("[" + new Timestamp(System.currentTimeMillis()) + "] username: "
                    + user.get().getUsername() + " 님이 접속하셨습니다.");
            log.info("현재 위치 : [" + messageDto.getLocation().toString() + "]");
            gomdoriService.sendMessage(messageDto);
        }
        else if(messageDto.getType().equals(MessageDto.MessageType.HELP)){
            log.info("============= 곰돌이 현재 위치 전송 =============");
            log.info("도움 요청서 : " + messageDto.getHelpRequestDto());
            gomdoriService.sendMessage(messageDto);
        }
//        else if(messageDto.getType().equals(MessageDto.MessageType.ACCEPT)){
//            log.info("============= 도움 곰돌이 매칭 성사!! =============");
//            gomdoriService.sendMessage(messageDto);
//        }
    }

    // 매칭 되었을 때
//    @SubscribeMapping
//    public void matching(){
//
//    }

    // 곰돌이가 도움 요청할 때 방 생성
    @MessageMapping("/help") // 메인 맵에다가 자신의 위치 및 도움 필요 정보 뿌림
    public void reqHelp(MessageDto messageDto){
        log.info("========== 곰돌이 도움 요청 !!! ==========");
//        HelpMessage help = gomdoriService.createHelp(helpMessage);
//        log.info("[도움 요청 정보] : "+help);
        gomdoriService.sendMessage(messageDto);
    }

}
