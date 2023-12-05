package kosta.main.chats.WebSocketConfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    log.info("{}", registry);
    // SockJS 지원을 활성화하여 WebSocket이 사용 불가능한 경우에도 사용할 수 있도록 설정
    registry.addEndpoint("/ws")
        .setAllowedOrigins("*") // 모든 Origin 허용 (보안상의 이유로 제한적으로 설정하는 것이 좋음)
        .withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    // 메시지 브로커가 처리할 경로의 접두어 설정
    registry.enableSimpleBroker("/sub");
    // 클라이언트가 메시지를 보낼 때 사용할 경로의 접두어 설정
    registry.setApplicationDestinationPrefixes("/pub");
  }
}
