package bf.multi.server.controller.websocket;

import bf.multi.server.domain.dto.websocket.MessageDto;
import bf.multi.server.domain.user.User;
import bf.multi.server.security.JwtTokenProvider;
import bf.multi.server.service.UserService;
import bf.multi.server.service.websocket.GomdoriService;
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
    @MessageMapping("/connecting")
    public void initEnter(MessageDto messageDto) throws InterruptedException {
        if (messageDto.getType().equals(MessageDto.MessageType.ENTER)) {
            String username = jwtTokenProvider.getUsernameByToken(messageDto.getJwt());
            User user = userService.loadUserByUsername(username);
            log.info("============= 베:프 현재 위치 전송 =============");
            log.info("[" + new Timestamp(System.currentTimeMillis()) + "] username: "
                    + user.getUsername() + " 님이 접속하셨습니다.");
            log.info("현재 위치 : [" + messageDto.getLocation().toString() + "]");
            gomdoriService.sendMessage(messageDto);
        } else if (messageDto.getType().equals(MessageDto.MessageType.HELP)) {
            log.info("============= 곰돌이 현재 위치 전송 =============");
            log.info("도움 요청서 : " + messageDto.getHelpRequest().toString());
            gomdoriService.sendMessage(messageDto);
        }
        else if(messageDto.getType().equals(MessageDto.MessageType.ACCEPT)) {
            log.info("============= 곰돌이 & 베:프 매칭 =============");
            gomdoriService.updateHelpsAndRequests(messageDto);
            Thread.sleep(500);
            gomdoriService.sendMessage(messageDto);
            // TODO: 매칭 되고 모든 유저에게 다시 메세지 전송해줘야하나?
        }
    }

}
