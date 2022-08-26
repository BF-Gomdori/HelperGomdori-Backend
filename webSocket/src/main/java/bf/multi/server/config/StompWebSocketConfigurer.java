package bf.multi.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class StompWebSocketConfigurer implements WebSocketMessageBrokerConfigurer {
    /**
     * Configure message broker options.
     *
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        /**
         * /pub 경로로 시작하는 STOMP 메세지의 "destination" 헤더는 @Controller 객체의 @MessageMapping 메서드로 라우팅된다.
         *  내장된 메세지 브로커를 사용해 Client에게 Subscriptions, Broadcasting 기능을 제공한다.
         *  또한 /sub로 시작하는 "destination" 헤더를 가진 메세지를 브로커로 라우팅한다.
         */
        registry
                .setApplicationDestinationPrefixes("/pub")
                .enableSimpleBroker("/sub");
    }

    /**
     * Register STOMP endpoints mapping each to a specific URL and (optionally)
     * enabling and configuring SockJS fallback options.
     *
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/gomdori")
                .setAllowedOrigins("http://localhost:9001")
                .withSockJS();
    }
}
