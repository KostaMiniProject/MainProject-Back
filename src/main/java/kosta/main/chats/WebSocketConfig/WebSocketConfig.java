package kosta.main.chats.WebSocketConfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    // SockJS 지원을 활성화하여 WebSocket이 사용 불가능한 경우에도 사용할 수 있도록 설정
    registry.addEndpoint("/ws")
        .setAllowedOriginPatterns("http://*", "https://*")
        .withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    // 메시지 브로커가 처리할 경로의 접두어 설정
    registry.enableSimpleBroker("/sub");
    // 클라이언트가 메시지를 보낼 때 사용할 경로의 접두어 설정
    registry.setApplicationDestinationPrefixes("/pub");
  }
  @Override
  public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
    registration.addDecoratorFactory(handler -> new WebSocketHandlerDecorator(handler) {
      @Override
      public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocket 연결 성립: {}", session);
        super.afterConnectionEstablished(session);
      }

      @Override
      public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("WebSocket 연결 해제: {}", session);
        super.afterConnectionClosed(session, closeStatus);
      }

      // 필요한 경우 onMessage, onTransportError 등의 다른 메서드도 오버라이드할 수 있습니다.
    });
  }
}
