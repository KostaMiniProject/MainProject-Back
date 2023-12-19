package kosta.main.chatrooms.service;

import kosta.main.bids.entity.Bid;
import kosta.main.bids.repository.BidRepository;
import kosta.main.chatrooms.dto.*;
import kosta.main.chatrooms.entity.ChatRoom;
import kosta.main.chatrooms.repository.ChatRoomsRepository;
import kosta.main.chats.entity.Chat;
import kosta.main.chats.repository.ChatsRepository;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.exchangeposts.repository.ExchangePostsRepository;
import kosta.main.items.entity.Item;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ChatRoomService {
  private final ChatRoomsRepository chatRoomsRepository;
  private final ChatsRepository chatsRepository;
  private final UsersRepository usersRepository;
  private final ExchangePostsRepository exchangePostsRepository;
  private final BidRepository bidRepository;
  private final SimpMessageSendingOperations messagingTemplate;



  // 공통 메서드 : Repository에서 id를 기준으로 찾는 로직
  private <T> T findEntityById(JpaRepository<T, Integer> repository, Integer id, String errorMessage) {
    return repository.findById(id)
        .orElseThrow(() -> new RuntimeException(errorMessage));
  }

  // 채팅방 생성
  public CreateChatRoomResponseDTO createChatRoom(CreateChatRoomDTO createChatRoomDTO, User sender) {
    // bidId로 채팅방 존재 여부 확인
    Optional<ChatRoom> existingChatRoom = chatRoomsRepository.findByBid_BidId(createChatRoomDTO.getBidId());
    if (existingChatRoom.isPresent()) {
      // 이미 존재하는 채팅방의 정보를 반환
      return CreateChatRoomResponseDTO.from(existingChatRoom.get());
    } else {
      // 새 채팅방 생성
      Bid bid = findEntityById(bidRepository, createChatRoomDTO.getBidId(), "Bid not found");
      User receiver = findEntityById(usersRepository, bid.getUser().getUserId(), "Receiver not found");
      ExchangePost exchangePost = findEntityById(exchangePostsRepository, bid.getExchangePost().getExchangePostId(), "ExchangePost not found");
      if(!sender.getUserId().equals(exchangePost.getUser().getUserId())){
        new RuntimeException("교환 게시글의 작성자만 채팅방을 개설할 수 있습니다!");
      }

        ChatRoom chatRoom = ChatRoom.of(exchangePost, bid, sender, receiver);

      // 초기 시스템 메시지 생성
      Chat initialChat = Chat.builder()
          .chatRoom(chatRoom)
          .message(sender.getName() + "님이 " + receiver.getName() + "과의 방을 생성 하셨습니다.")
          .user(sender) // 시스템 메시지이므로 사용자는 null
          .isRead(false)
          .build();

      // 채팅방 저장
      chatRoomsRepository.save(chatRoom);

      // 초기 메시지 저장
      chatsRepository.save(initialChat);

      return CreateChatRoomResponseDTO.from(chatRoom);
    }
  }


  // 채팅방 나가기
  public void leaveChatRoom(Integer chatRoomId, User user) {
    ChatRoom chatRoom = findEntityById(chatRoomsRepository, chatRoomId, "ChatRoom not found with id: " + chatRoomId);

    // Optional을 사용하여 null 체크 수행
    boolean isSender = Optional.ofNullable(chatRoom.getSender())
        .map(User::getUserId)
        .filter(userId -> userId.equals(user.getUserId()))
        .isPresent();

    boolean isReceiver = Optional.ofNullable(chatRoom.getReceiver())
        .map(User::getUserId)
        .filter(userId -> userId.equals(user.getUserId()))
        .isPresent();

    if (!isSender && !isReceiver) {
      throw new RuntimeException("User does not belong to the chat room");
    }

    if ((isSender && chatRoom.getReceiver() == null) || (isReceiver && chatRoom.getSender() == null)) {
      // 모든 유저가 나갔다면 채팅방은 삭제 처리 된다.
      chatRoomsRepository.delete(chatRoom);
    } else {
      if (isSender) { // 교환 게시글 작성자가 나갔다면 해당 채팅방의 Sender를 null로 처리한다.
        chatRoom.updateSender(null);
      } else { // 입찰자가 나갔다면 Receiver를 null로 처리한다.
        chatRoom.updateReceiver(null);
      }
      chatRoomsRepository.save(chatRoom);

      // Send system message to the remaining user
      String systemMessageContent = "User :" + user.getName() + " has left the chat room.";
//      sendMessageSystem(chatRoomId, systemMessageContent);
    }
  }


  // 채팅방 목록 조회
  public List<ChatRoomResponseDTO> getChatRooms(User currentUser) {
    List<ChatRoom> chatRooms = chatRoomsRepository.findBySenderUserIdOrReceiverUserId(currentUser.getUserId(), currentUser.getUserId());

    // 채팅방을 마지막 메시지 시간에 따라 내림차순으로 정렬
    return chatRooms.stream()
        .map(chatRoom -> ChatRoomResponseDTO.of(chatRoom, currentUser))
        .sorted(Comparator.comparing(ChatRoomResponseDTO::getLastMessageTimeDifference).reversed())
        .collect(Collectors.toList());
  }

  // 특정 채팅방의 채팅 내역을 불러오는 기능
  public ChatRoomEnterResponseDTO getChatList(Integer chatRoomId, User user, Pageable pageable) {
    ChatRoom chatRoom = findEntityById(chatRoomsRepository, chatRoomId, "ChatRoom Not Found");

    User otherUser = chatRoom.getSender().getUserId().equals(user.getUserId()) ? chatRoom.getReceiver() : chatRoom.getSender();

    ExchangePost exchangePost = chatRoom.getExchangePost();
    Item exchangePostItem = exchangePost.getItem();
    String exchangePostImage = exchangePostItem.getImages().isEmpty() ? null : exchangePostItem.getImages().get(0);
    String exchangePostCategory = exchangePostItem.getCategory() != null ? exchangePostItem.getCategory().getCategoryName() : null;
    Bid bid = findEntityById(bidRepository, chatRoom.getBid().getBidId(), "bid Not Found");
    Page<Chat> chats = chatsRepository.findByChatRoom(chatRoom, pageable);
    List<ChatRoomEnterResponseDTO.ChatMessageResponseDTO> chatMessageResponseDTOList = chats.stream()
        .map(chat -> ChatRoomEnterResponseDTO.ChatMessageResponseDTO.builder()
            .chatId(chat.getChatId())
            .senderId(chat.getUser().getUserId())
            .content(Optional.ofNullable(chat.getMessage()))
            .imageUrl(Optional.ofNullable(chat.getChatImage()))
            .createAt(chat.getCreatedAt().toString()) // 여기서 날짜 형식을 조정할 수 있습니다.
            .isRead(chat.isRead())
            .build())
        .collect(Collectors.toList());

    for (Chat chat : chats) {
      if (!chat.getUser().getUserId().equals(user.getUserId()) && !chat.isRead()) {
        chat.updateIsRead(true); // 본인이 보낸 메시지가 아닌 경우 읽음 처리
        chatsRepository.save(chat); // 변경된 상태 저장
      }
    }
    return ChatRoomEnterResponseDTO.builder()
        .exchangePostId(exchangePost.getExchangePostId())
        .exchangePostTittle(exchangePost.getTitle())
        .exchangePostAddress(exchangePost.getAddress())
        .exchangePostCategory(exchangePostCategory)
        .exchangePostImage(exchangePostImage)
        .status(bid.getStatus())
        .bidId(bid.getBidId())
        .userId(otherUser.getUserId())
        .userName(otherUser.getName())
        .userProfileImage(otherUser.getProfileImage())
        .messages(chatMessageResponseDTOList)
        .build();
  }



  // 채팅방 입장 알림
  public void notifyChatRoomEntry(Integer chatRoomId, Integer userId) {
    // 채팅방 입장 알림 로직
    // ...

    // 알림 관련 로직 구현
  }

  // 시스템 메세지를 날리기 위한 로직
//  private void sendMessageSystem(Integer chatRoomId, String content) {
//    // 시스템 메시지 생성
//    ChatMessageDTO systemMessage = new ChatMessageDTO();
//    systemMessage.setContent(content);
//    systemMessage.setChatRoomId(chatRoomId);
//    systemMessage.setSenderId(null); // 시스템 메시지이므로 발신자 ID는 null
//
//    // 채팅방 구독자에게 시스템 메시지 전송
//    String destination = "/sub/chatroom/" + chatRoomId;
//    messagingTemplate.convertAndSend(destination, systemMessage);
//  }


}
