package kosta.main.chats.controller;

import kosta.main.chats.dto.ChatMessageDTO;
import kosta.main.chats.service.ChatsService;
import kosta.main.users.entity.LoginUser;
import kosta.main.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatsController {
  private final ChatsService chatsService;
  @MessageMapping("/chat/send")
  @SendTo("/sub/messages")
  public ChatMessageDTO send(ChatMessageDTO chatMessage, @LoginUser User user) {
    // 채팅 메시지 저장 및 처리
    chatsService.saveChat(chatMessage, user);
    return chatMessage;
  }

}
