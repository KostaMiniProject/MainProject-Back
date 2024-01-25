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
import kosta.main.global.error.exception.BusinessException;
import kosta.main.global.error.exception.CommonErrorCode;
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
      ChatRoom chatRoom = existingChatRoom.get();

      // 채팅방의 참가자가 나갔을 경우를 처리
      if (chatRoom.getSender() == null) {
        chatRoom.updateSender(sender);
      } else if (chatRoom.getReceiver() == null) {
        chatRoom.updateReceiver(sender);
      }

      // 변경된 채팅방 저장
      chatRoomsRepository.save(chatRoom);

      return CreateChatRoomResponseDTO.from(chatRoom);
    } else {
      // 새 채팅방 생성
      Bid bid = findEntityById(bidRepository, createChatRoomDTO.getBidId(), "Bid not found");
      User receiver = findEntityById(usersRepository, bid.getUser().getUserId(), "Receiver not found");
      ExchangePost exchangePost = findEntityById(exchangePostsRepository, bid.getExchangePost().getExchangePostId(), "ExchangePost not found");

      if (!sender.getUserId().equals(exchangePost.getUser().getUserId())) {
        throw new RuntimeException("교환 게시글의 작성자만 채팅방을 개설할 수 있습니다!");
      }

      ChatRoom chatRoom = ChatRoom.of(exchangePost, bid, sender, receiver);

      // 초기 시스템 메시지 생성
      Chat initialChat = Chat.builder()
          .chatRoom(chatRoom)
          .message(sender.getName() + "님이 " + receiver.getName() + "님과의 방을 생성 하셨습니다.")
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

    Optional<User> optionalSender = Optional.ofNullable(chatRoom.getSender());
    Optional<User> optionalReceiver = Optional.ofNullable(chatRoom.getReceiver());

    if (optionalSender.map(User::getUserId).filter(userId -> userId.equals(user.getUserId())).isPresent()) {
      chatRoom.updateSender(null); // 교환 게시글 작성자가 나갔다면 Sender를 null로 처리
    } else if (optionalReceiver.map(User::getUserId).filter(userId -> userId.equals(user.getUserId())).isPresent()) {
      chatRoom.updateReceiver(null); // 입찰자가 나갔다면 Receiver를 null로 처리
    } else {
      throw new RuntimeException("User does not belong to the chat room");
    }

    if (!optionalSender.isPresent() && !optionalReceiver.isPresent()) {
      // 모든 유저가 나갔다면 채팅방 삭제
      chatRoomsRepository.delete(chatRoom);
    } else {
      // 변경 사항 저장
      chatRoomsRepository.save(chatRoom);
    }
  }



  // 채팅방 목록 조회
  public List<ChatRoomResponseDTO> getChatRooms(User currentUser) {
    List<ChatRoom> chatRooms = chatRoomsRepository.findBySenderUserId(currentUser.getUserId(), currentUser.getUserId());

    // 채팅방을 마지막 메시지 시간에 따라 내림차순으로 정렬
    return chatRooms.stream()
        .map(chatRoom -> ChatRoomResponseDTO.of(chatRoom, currentUser))
        .sorted(Comparator.comparing(ChatRoomResponseDTO::getLastMessageDateTime).reversed())
        .collect(Collectors.toList());
  }

  // 특정 채팅방의 채팅 내역을 불러오는 기능
  public ChatRoomEnterResponseDTO getChatList(Integer chatRoomId, User user, Pageable pageable) {
    ChatRoom chatRoom = findEntityById(chatRoomsRepository, chatRoomId, "ChatRoom Not Found");

    User otherUser = Optional.ofNullable(chatRoom.getSender())
        .filter(sender -> !sender.getUserId().equals(user.getUserId()))
        .orElse(chatRoom.getReceiver());

    ExchangePost exchangePost = chatRoom.getExchangePost();
    Item exchangePostItem = exchangePost.getItem();
    String exchangePostImage = exchangePostItem.getImages().isEmpty() ? null : exchangePostItem.getImages().get(0);
    String exchangePostCategory = exchangePostItem.getCategory() != null ? exchangePostItem.getCategory().getCategoryName() : null;
    Bid bid = findEntityById(bidRepository, chatRoom.getBid().getBidId(), "bid Not Found");
    Page<Chat> chats = chatsRepository.findByChatRoom(chatRoom, pageable);

    List<ChatRoomEnterResponseDTO.ChatMessageResponseDTO> chatMessageResponseDTOList = chats.stream()
        .map(chat -> {
          Integer senderId = Optional.ofNullable(chat.getUser())
              .map(User::getUserId)
              .orElse(null); // User가 null일 경우를 대비

          return ChatRoomEnterResponseDTO.ChatMessageResponseDTO.builder()
              .chatId(chat.getChatId())
              .senderId(senderId)
              .content(Optional.ofNullable(chat.getMessage()))
              .imageUrl(Optional.ofNullable(chat.getChatImage()))
              .createAt(chat.getCreatedAt().toString())
              .isRead(chat.isRead())
              .build();
        })
        .collect(Collectors.toList());


    // 메시지 읽음 처리
    for (Chat chat : chats) {
      Optional.ofNullable(chat.getUser())
          .map(User::getUserId)
          .ifPresent(userId -> {
            if (!userId.equals(user.getUserId()) && !chat.isRead()) {
              chat.updateIsRead(true);
              chatsRepository.save(chat);
            }
          });
    }
    return ChatRoomEnterResponseDTO.builder()
        .isOwner(user.getUserId().equals(exchangePost.getUser() != null ? exchangePost.getUser().getUserId() : null))
        .exchangePostId(exchangePost.getExchangePostId())
        .exchangePostTittle(exchangePost.getTitle())
        .exchangePostAddress(exchangePost.getAddress())
        .exchangePostCategory(exchangePostCategory) // 이전에 null 체크를 이미 수행했다고 가정
        .exchangePostImage(exchangePostImage) // 이전에 null 체크를 이미 수행했다고 가정
        .status(bid.getStatus())
        .bidId(bid.getBidId())
        .userId(otherUser != null ? otherUser.getUserId() : null)
        .userName(otherUser != null ? otherUser.getName() : null)
        .userProfileImage(otherUser != null ? otherUser.getProfileImage() : null)
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
