package kosta.main.chatrooms.controller;

import kosta.main.chatrooms.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatRooms")
@RequiredArgsConstructor
public class ChatRoomsController {
  private final ChatRoomService chatRoomService;

}
