package kosta.main.chats.controller;

import kosta.main.chats.service.ChatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatsController {
  private final ChatsService chatsService;

}
