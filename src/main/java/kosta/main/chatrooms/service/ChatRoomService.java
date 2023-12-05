package kosta.main.chatrooms.service;

import kosta.main.chatrooms.dto.CreateChatRoomDTO;
import kosta.main.chatrooms.entity.ChatRoom;
import kosta.main.chatrooms.repository.ChatRoomsRepository;
import kosta.main.exchangeposts.dto.ExchangePostResponseDTO;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.exchangeposts.repository.ExchangePostsRepository;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class ChatRoomService {
  private final ChatRoomsRepository chatRoomsRepository;
  private final UsersRepository usersRepository;
  private final ExchangePostsRepository exchangePostsRepository;


  // 채팅방 생성
  public ChatRoom createChatRoom(CreateChatRoomDTO createChatRoomDTO, User sender) {
    User receiver = usersRepository.findById(createChatRoomDTO.getReceiverId())
        .orElseThrow(() -> new RuntimeException("Receiver not found"));
    ExchangePost exchangePost = exchangePostsRepository.findById(createChatRoomDTO.getExchangePostId())
        .orElseThrow(() -> new RuntimeException("ExchangePost not found"));
    ChatRoom chatRoom = new ChatRoom();
    chatRoom.updateExchangePost(exchangePost);
    chatRoom.updateSender(sender);
    chatRoom.updateReceiver(receiver);

    return chatRoomsRepository.save(chatRoom);
  }

  // 채팅방 삭제
  public void deleteChatRoom(Integer chatRoomId, User user) {
    // 채팅방 삭제 로직
    // ...

    chatRoomsRepository.deleteById(chatRoomId);
  }

  // 채팅방 목록 조회
  public List<ChatRoom> getChatRooms(Integer userId) {
    // 사용자가 속한 채팅방 목록 조회 로직
    // ...

    return chatRoomsRepository.findBySenderUserIdOrReceiverUserId(userId, userId);
  }

  // 채팅방 입장 알림
  public void notifyChatRoomEntry(Integer chatRoomId, Integer userId) {
    // 채팅방 입장 알림 로직
    // ...

    // 알림 관련 로직 구현
  }

}
