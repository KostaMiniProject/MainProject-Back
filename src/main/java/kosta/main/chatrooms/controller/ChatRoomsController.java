package kosta.main.chatrooms.controller;

import kosta.main.chatrooms.dto.*;
import kosta.main.chatrooms.entity.ChatRoom;
import kosta.main.chatrooms.service.ChatRoomService;
import kosta.main.chats.entity.Chat;
import kosta.main.users.entity.LoginUser;
import kosta.main.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatRooms")
@RequiredArgsConstructor
public class ChatRoomsController {
  private final ChatRoomService chatRoomService;
  // 채팅방 생성 (클라이언트에서 바로 구독이 가능하도록 ResponseDTO 구성)
  @PostMapping
  public ResponseEntity<CreateChatRoomResponseDTO> createChatRoom(@RequestBody CreateChatRoomDTO createChatRoomDTO, @LoginUser User user) {
    CreateChatRoomResponseDTO createChatRoomResponseDTO = chatRoomService.createChatRoom(createChatRoomDTO, user);
    return new ResponseEntity<>(createChatRoomResponseDTO, HttpStatus.CREATED);
  }

  // 채팅방 삭제
  @DeleteMapping("/{chatRoomId}")
  public ResponseEntity<Void> leaveChatRoom(@PathVariable Integer chatRoomId, @LoginUser User user) {
    chatRoomService.leaveChatRoom(chatRoomId, user);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  // 채팅방 목록 조회
  @GetMapping
  public ResponseEntity<List<ChatRoomResponseDTO>> getChatRooms(@LoginUser User user) {
    List<ChatRoomResponseDTO> chatRooms = chatRoomService.getChatRooms(user);
    return new ResponseEntity<>(chatRooms, HttpStatus.OK);
  }

  @GetMapping("/{chatRoomId}")
  public ResponseEntity<?> getChatList(@PathVariable("chatRoomId") Integer chatRoomId,
                                       @LoginUser User user,
                                       @PageableDefault(page = 0, size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

    ChatRoomEnterResponseDTO chatList = chatRoomService.getChatList(chatRoomId, user, pageable);

    // Page 객체가 더 이상 내용이 없을 경우, 즉 데이터가 없는 경우 처리
    if (!chatList.getMessages().isEmpty()) {
      // 메시지가 있으면 정상적으로 응답
      return ResponseEntity.ok(chatList);
    } else {
      // 메시지가 없으면 더 이상 페이지가 없다는 상태(204 No Content)를 반환
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No more pages available");
    }
  }

  // 채팅방 입장 알림
  @PostMapping("/{chatRoomId}/notify-entry")
  public ResponseEntity<Void> notifyChatRoomEntry(@PathVariable Integer chatRoomId, @LoginUser User user) {
    chatRoomService.notifyChatRoomEntry(chatRoomId, user.getUserId());
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
