package bf.multi.server.websocket.controller;

import bf.multi.server.websocket.domain.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.sql.Timestamp;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GreetingController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/greeting")
    public void handle(MessageDto message){
        simpMessageSendingOperations.convertAndSend("/sub/ch/"+message.getReceiverId(),message);
        log.info("["+ new Timestamp(System.currentTimeMillis()) +": "+message.getSenderId()+"가 입장했음");
    }
}
