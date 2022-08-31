package bf.multi.server.controller.websocket;

import bf.multi.server.domain.dto.websocket.HelperPingDto;
import bf.multi.server.domain.helps.Helps;
import bf.multi.server.domain.requests.Requests;
import bf.multi.server.security.JwtTokenProvider;
import bf.multi.server.domain.dto.websocket.MessageDto;
import bf.multi.server.service.websocket.FindConnectedUsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Api & WebSocket", description = "메인 화면 첫 접속 시 이미 접속해있는 ping들을 받기 위한 API")
public class ServerSendMessageController {

    private final SimpMessagingTemplate template;
    private final JwtTokenProvider jwtTokenProvider;
    private final FindConnectedUsersService findConnectedUsersService;

    @Tag(name = "Api & WebSocket")
    @Operation(summary = "/user/map/main subscribe 직후 요청 동작이 있어야함",
            description = "Header 설정(value 넣을 때 Beaer 다음에 띄어쓰기 한칸 중요!) : [Authorization]:[Bearer {기본 회원 가입 시 받은 JWT}]")
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
