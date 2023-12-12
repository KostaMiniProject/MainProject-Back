package kosta.main.chats.service;

import jakarta.persistence.EntityNotFoundException;
import kosta.main.chatrooms.dto.ChatListResponseDTO;
import kosta.main.chatrooms.entity.ChatRoom;
import kosta.main.chatrooms.repository.ChatRoomsRepository;
import kosta.main.chats.dto.*;
import kosta.main.chats.entity.Chat;
import kosta.main.chats.repository.ChatsRepository;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class ChatsService {

  private final ChatRoomsRepository chatRoomsRepository;
  private final ChatsRepository chatsRepository;
  private final UsersRepository usersRepository;

  public ChatMessageResponseDTO saveChat(ChatMessageDTO chatMessage) {
    ChatRoom chatRoom = chatRoomsRepository.findById(chatMessage.getRoomId())
        .orElseThrow(() -> new RuntimeException("ChatRoom not found"));
    User sender = usersRepository.findById(chatMessage.getSenderId())
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + chatMessage.getSenderId()));
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    Chat chat = new Chat();
    if(Optional.ofNullable(chatMessage.getImageUrl()).isPresent()){
      chatMessage.getImageUrl().ifPresent(chat::updateImageUrl);
    }else{
      chatMessage.getContent().ifPresent(chat::updateMessage);
    }
    chat.updateUser(sender);
    chat.updateChatRoom(chatRoom);
    chatsRepository.save(chat);

    return ChatMessageResponseDTO.builder()
        .chatId(chat.getChatId())
        .senderId(sender.getUserId())
        .content(Optional.ofNullable(chat.getMessage()))
        .imageUrl(Optional.ofNullable(chat.getChatImage()))
        .createAt(chat.getCreatedAt().format(formatter)) // 날짜 형식 변환 적용
        .isRead(chat.isRead())
        .build();
  }


  public ChatIdResponseDTO markAsRead(ChatIdDTO chatIdDTO) {
    Chat chat = chatsRepository.findById(chatIdDTO.getChatId())
        .orElseThrow(() -> new EntityNotFoundException("Chat not found with id: " + chatIdDTO));
    chat.updateIsRead(true);
    chatsRepository.save(chat);
    return ChatIdResponseDTO.builder()
        .chatId(chat.getChatId())
        .chatRoomId(chat.getChatRoom().getChatRoomId())
        .build();
  }

  public ChatMessageResponseDTO sendSystemMessage(ChatRoomEventDTO eventDTO, String eventType) {
    // 채팅방 정보 조회
    ChatRoom chatRoom = chatRoomsRepository.findById(eventDTO.getChatRoomId())
        .orElseThrow(() -> new EntityNotFoundException("ChatRoom not found with id: " + eventDTO.getChatRoomId()));

    // 시스템 메시지 생성
    String systemMessage;
    if ("ENTER".equals(eventType)) {
      systemMessage = eventDTO.getUserName() + "님이 입장하셨습니다.";
    } else if ("EXIT".equals(eventType)) {
      systemMessage = eventDTO.getUserName() + "님이 퇴장하셨습니다.";
    } else {
      throw new IllegalArgumentException("Invalid event type: " + eventType);
    }

    // Chat 엔터티 생성 및 저장
    Chat chat = Chat.builder()
        .chatRoom(chatRoom)
        .message(systemMessage)
        .user(null) // 시스템 메시지이므로 발신자는 null
        .build();
    chatsRepository.save(chat);

    // 응답 객체 생성
    return ChatMessageResponseDTO.builder()
        .chatId(chat.getChatId())
        .content(Optional.of(systemMessage))
        .createAt(chat.getCreatedAt().toString())
        .isRead(false) // 초기에는 읽지 않음 상태
        .build();
  }





}
