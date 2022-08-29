package bf.multi.server.websocket.handler;

import bf.multi.server.domain.user.User;
import bf.multi.server.domain.user.UserRepository;
import bf.multi.server.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        log.info("WebSocket Message : "+String.valueOf(message.getHeaders()));
        StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if(StompCommand.CONNECT.equals(accessor.getCommand())) {
            if(!jwtTokenProvider.validateToken(accessor.getFirstNativeHeader("Authorization")))
                throw new AccessDeniedException("");
            /////
            String jwt = accessor.getFirstNativeHeader("Authorization");
            Optional<User> user = userRepository.findByUsername(jwtTokenProvider.getUsernameByToken(jwt));
            log.info("CONNECT [{}] : {}", jwtTokenProvider.getUsernameByToken(jwt), user);
            /////
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            Authentication auth = new UsernamePasswordAuthenticationToken(user.get().getUsername(), user.get().getPassword(), authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
            accessor.setUser(auth);
        }
        else if(StompCommand.SUBSCRIBE.equals(accessor.getCommand())){
            String roomId = getRoomId(
                    Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId")
            );
            log.info("SUBSCRIBE [{}] || 속해있는 방 : [{}]",String.valueOf(accessor.getUser().getName()),roomId);
        }
        else if(StompCommand.DISCONNECT.equals(accessor.getCommand())){ // 종료할 때
            String roomId = getRoomId(
                    Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId")
            );
            log.info("DISCONNECT [{}] : {}", accessor.getUser().getName(), roomId);
        }
        else if(StompCommand.SEND.equals(accessor.getCommand())){
            log.info("Send : "+String.valueOf(message));
        }
        return message;
    }

    public String getRoomId(String destination){
        int lastIndex = destination.lastIndexOf('/');
        if(lastIndex != -1)
            return destination.substring(lastIndex+1);
        else return "";
    }

}
