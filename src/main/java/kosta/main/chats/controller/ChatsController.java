package kosta.main.chats.controller;

import kosta.main.chatrooms.dto.ChatListResponseDTO;
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
//  @MessageMapping("/send")
//  public ChatMessageResponseDTO sendMessage(ChatMessageDTO chatMessage) {
//    // 채팅 메시지 저장 및 처리
//    Chat chat = chatsService.saveChat(chatMessage);
//    // ChatMessageResponseDTO 생성
//    ChatMessageResponseDTO responseDTO = ChatMessageResponseDTO.builder()
//        .content(chat.getMessage())
//        .build();
//
//    // destination을 ChatRoom에 해당하는 구독자로만 지정한다.
//    String destination = "/sub/chatroom/" + chatMessage.getChatRoomId();
//    messagingTemplate.convertAndSend(destination, responseDTO);
//    return responseDTO;
//  }

  @MessageMapping("/send")
  public ChatMessageResponseDTO sendMessage(ChatMessageDTO chatMessage) {
    ChatMessageResponseDTO response = chatsService.saveChat(chatMessage);
    String destination = "/sub/chatroom/" + chatMessage.getRoomId();
    messagingTemplate.convertAndSend(destination, response);
    return response;
  }



}
