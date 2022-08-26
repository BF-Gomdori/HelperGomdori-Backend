package bf.multi.server.websocket.controller;

import bf.multi.server.domain.user.User;
import bf.multi.server.security.JwtTokenProvider;
import bf.multi.server.service.UserService;
import bf.multi.server.websocket.domain.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class GreetingController {
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
