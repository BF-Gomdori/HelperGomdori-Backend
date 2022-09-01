package bf.multi.server.controller.firebase;

import bf.multi.server.domain.dto.firebase.FCMMessageDto;
import bf.multi.server.service.firebase.FCMService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/fcm")
public class FCMController {

    private final FCMService fcmService;

    @Tag(name = "FCM")
    @Operation(summary = "푸시 알림을 보내는 API", description = "Firebase로부터 토큰을 받아서 대상에게 푸시 알림 전송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "푸시 알림 성공")
    })
    @PostMapping("/push")
    public ResponseEntity pushMessage(@RequestBody FCMMessageDto fcmMessageDto) throws IOException {
        log.info(fcmMessageDto.getMessage().getToken(),
                fcmMessageDto.getMessage().getNotification().getTitle(),
                fcmMessageDto.getMessage().getNotification().getBody()
        );

        fcmService.sendMessageTo(
                fcmMessageDto.getMessage().getToken(),
                fcmMessageDto.getMessage().getNotification().getTitle(),
                fcmMessageDto.getMessage().getNotification().getBody()
        );

        return ResponseEntity.ok().build();
    }
}
