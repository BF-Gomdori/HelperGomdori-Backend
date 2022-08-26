package bf.multi.server.websocket.handler;

import bf.multi.server.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("WebSocket connect : "+String.valueOf(message));
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(accessor.getCommand() == StompCommand.CONNECT) {
            if(!jwtTokenProvider.validateToken(accessor.getFirstNativeHeader("Authorization")))
                throw new AccessDeniedException("");
//            else if(StompCommand.SUBSCRIBE == accessor.getCommand()){ // 앱에 접속하거나 도움 요청을 수락했을 때
//                String bearerToken = accessor.getFirstNativeHeader("Authorization");
//                log.info("WebSocket Connector : "+jwtTokenProvider.getUsernameByToken(bearerToken));
//            }
        }
        return message;
    }
}
