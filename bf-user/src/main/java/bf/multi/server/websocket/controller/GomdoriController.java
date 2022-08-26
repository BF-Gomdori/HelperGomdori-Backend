package bf.multi.server.websocket.controller;

import bf.multi.server.security.JwtTokenProvider;
import bf.multi.server.websocket.domain.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class GomdoriController {
    private final JwtTokenProvider jwtTokenProvider;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/helper")
    public void handle(MessageDto message){
        String jwt = message.getSenderId();
        String username = jwtTokenProvider.getUsernameByToken(jwt);
        log.info("============= Websocket msg =============");
        log.info("["+ new Timestamp(System.currentTimeMillis()) + "] username: "+username + " 님이 접속하셨습니다.");
        message.setSenderId(username);
        simpMessageSendingOperations.convertAndSend("/sub/ch/"+message.getReceiverId(),message);
    }
}
