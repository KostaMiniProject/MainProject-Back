package kosta.main.chats.controller;

import kosta.main.chats.dto.ChatMessageDTO;
import kosta.main.chats.service.ChatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatsController {
  private final ChatsService chatsService;
  private static final Logger LOGGER = LoggerFactory.getLogger( ChatsController.class );
  private final SimpMessageSendingOperations messagingTemplate;

  // 새로운 사용자가 웹 소켓을 연결할 때 실행됨
  // @EventListener은 한개의 매개변수만 가질 수 있다.
  @EventListener
  public void handleWebSocketConnectListener(SessionConnectEvent event) {
    LOGGER.info("Received a new web socket connection");
  }
  // 사용자가 웹 소켓 연결을 끊으면 실행됨
  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccesor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = headerAccesor.getSessionId();

    LOGGER.info("sessionId Disconnected : " + sessionId);
  }

  @MessageMapping("/send")
  public ChatMessageDTO sendMessage(ChatMessageDTO chatMessage) {
    // 채팅 메시지 저장 및 처리
    chatsService.saveChat(chatMessage);
    // destination을 ChatRoom에 해당하는 구독자로만 지정한다.
    String destination = "/sub/chatroom/" + chatMessage.getChatRoomId();
    messagingTemplate.convertAndSend(destination, chatMessage.getContent());
    return chatMessage;
  }

}
