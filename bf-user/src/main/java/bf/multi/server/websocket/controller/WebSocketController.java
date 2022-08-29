package bf.multi.server.websocket.controller;

import bf.multi.server.domain.user.User;
import bf.multi.server.security.JwtTokenProvider;
import bf.multi.server.service.UserService;
import bf.multi.server.websocket.domain.MessageDto;
import bf.multi.server.websocket.service.GomdoriService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.sql.Timestamp;

@Slf4j
@Controller
@RequiredArgsConstructor
@CrossOrigin
public class WebSocketController {
    private final UserService userService;
    private final GomdoriService gomdoriService;
    private final JwtTokenProvider jwtTokenProvider;

    // 자신의 위치 정보 표시한다고 눌렀을 때
    @MessageMapping("/enter")
    public void initEnter(MessageDto messageDto) {
        if(messageDto.getType().equals(MessageDto.MessageType.ENTER)) {
            String username = jwtTokenProvider.getUsernameByToken(messageDto.getHelperJwt());
            User user = userService.loadUserByUsername(username);
            log.info("============= 베:프 현재 위치 전송 =============");
            log.info("[" + new Timestamp(System.currentTimeMillis()) + "] username: "
                    + user.getUsername() + " 님이 접속하셨습니다.");
            log.info("현재 위치 : [" + messageDto.getLocation().toString() + "]");
            gomdoriService.sendMessage(messageDto);
        }
        else if(messageDto.getType().equals(MessageDto.MessageType.HELP)){
            log.info("============= 곰돌이 현재 위치 전송 =============");
            log.info("도움 요청서 : " + messageDto.getHelpRequest());
            gomdoriService.sendMessage(messageDto);
        }
    }

    // 곰돌이가 도움 요청할 때 방 생성
    @MessageMapping("/help") // 메인 맵에다가 자신의 위치 및 도움 필요 정보 뿌림
    public void reqHelp(MessageDto messageDto){
        log.info("========== 곰돌이 도움 요청 !!! ==========");
//        HelpMessage help = gomdoriService.createHelp(helpMessage);
//        log.info("[도움 요청 정보] : "+help);
        gomdoriService.sendMessage(messageDto);
    }

}
