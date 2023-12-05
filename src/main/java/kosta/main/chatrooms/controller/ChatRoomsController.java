package kosta.main.chatrooms.controller;

import kosta.main.chatrooms.CreateChatRoomDTO;
import kosta.main.chatrooms.entity.ChatRoom;
import kosta.main.chatrooms.service.ChatRoomService;
import kosta.main.users.entity.LoginUser;
import kosta.main.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatRooms")
@RequiredArgsConstructor
public class ChatRoomsController {
  private final ChatRoomService chatRoomService;
  // 채팅방 생성
  @PostMapping
  public ResponseEntity<ChatRoom> createChatRoom(@RequestBody CreateChatRoomDTO createChatRoomDTO, @LoginUser User user) {
    ChatRoom chatRoom = chatRoomService.createChatRoom(createChatRoomDTO, user);
    return new ResponseEntity<>(chatRoom, HttpStatus.CREATED);
  }

  // 채팅방 삭제
  @DeleteMapping("/{chatRoomId}")
  public ResponseEntity<Void> deleteChatRoom(@PathVariable Integer chatRoomId,  @LoginUser User user) {
    chatRoomService.deleteChatRoom(chatRoomId, user);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  // 채팅방 목록 조회
  @GetMapping
  public ResponseEntity<List<ChatRoom>> getChatRooms(@LoginUser User user) {
    List<ChatRoom> chatRooms = chatRoomService.getChatRooms(user.getUserId());
    return new ResponseEntity<>(chatRooms, HttpStatus.OK);
  }

  // 채팅방 입장 알림
  @PostMapping("/{chatRoomId}/notify-entry")
  public ResponseEntity<Void> notifyChatRoomEntry(@PathVariable Integer chatRoomId, @LoginUser User user) {
    chatRoomService.notifyChatRoomEntry(chatRoomId, user.getUserId());
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
