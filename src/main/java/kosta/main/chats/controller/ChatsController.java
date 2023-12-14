package kosta.main.chats.controller;

import kosta.main.chatrooms.dto.ChatListResponseDTO;
import kosta.main.chats.dto.ChatIdDTO;
import kosta.main.chats.dto.ChatIdResponseDTO;
import kosta.main.chats.dto.ChatMessageDTO;
import kosta.main.chats.dto.ChatMessageResponseDTO;
import kosta.main.chats.service.ChatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatsController {
  private final ChatsService chatsService;
  private final SimpMessageSendingOperations messagingTemplate;

  
  // 클라이언트가 메세지를 발행하면 서버에서 적절한 채팅방으로만 메시지를 전달한다
  @MessageMapping("/send")
  public ChatMessageResponseDTO sendMessage(ChatMessageDTO chatMessage) {
    ChatMessageResponseDTO response = chatsService.saveChat(chatMessage);
    String destination = "/sub/chatroom/" + chatMessage.getRoomId();
    messagingTemplate.convertAndSend(destination, response);
    return response;
  }

  // 클라이언트가 WebSocket에 연결된 상태에서 chatId를 제공하면 해당 chatId를 읽음 처리하는 기능
  // 단 본인이 보낸 채팅은 읽음처리 해선 안된다.
  @MessageMapping("/read")
  public ChatIdResponseDTO markMessageAsRead(ChatIdDTO chatIdDTO) {
    ChatIdResponseDTO responseDTO = chatsService.markAsRead(chatIdDTO);
    String destination = "/sub/chatroom/" + responseDTO.getChatRoomId();
    messagingTemplate.convertAndSend(destination, responseDTO);
    return responseDTO;
  }

}
