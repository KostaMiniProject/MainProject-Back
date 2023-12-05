package kosta.main.chats.controller;

import kosta.main.chats.dto.ChatMessageDTO;
import kosta.main.chats.service.ChatsService;
import kosta.main.users.entity.LoginUser;
import kosta.main.users.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatsController {
  private final ChatsService chatsService;
  @MessageMapping("/send")
  @SendTo("/sub/messages")
  public ChatMessageDTO send(ChatMessageDTO chatMessage, @LoginUser User user) {
    log.debug("Received message: {}", chatMessage);
    // 채팅 메시지 저장 및 처리
    ChatMessageDTO response = chatsService.saveChat(chatMessage, user);
    log.debug("Sending response: {}", response);
    return chatMessage;
  }

}
