package bf.multi.server.websocket.controller;

import bf.multi.server.domain.helps.Helps;
import bf.multi.server.domain.requests.Requests;
import bf.multi.server.security.JwtTokenProvider;
import bf.multi.server.websocket.domain.MessageDto;
import bf.multi.server.websocket.service.FindConnectedUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ServerSendMessageController {

    private final SimpMessagingTemplate template;
    private final JwtTokenProvider jwtTokenProvider;
    private final FindConnectedUsersService findConnectedUsersService;

    @GetMapping("/send")
    public void sendMessage(HttpServletRequest request) { // connect할때 setUser했던걸로 가는듯? jwt 받아서 username 가져와서 정보 뿌려줘야할듯
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String jwt = request.getHeader("Authorization")
                .substring(7, request.getHeader("Authorization").length());
        String username = jwtTokenProvider.getUsernameByToken(jwt);

        List<Helps> helpsList = findConnectedUsersService
                .loadHelpsBeforeConnectedUser(now);
        List<Requests> requestsList = findConnectedUsersService
                .loadRequestsBeforeConnectedUser(now);
        List<MessageDto> messageDtos = findConnectedUsersService.composeInitData(helpsList, requestsList);

        this.template.convertAndSendToUser(username,"/map/main",  messageDtos);
    }

}
