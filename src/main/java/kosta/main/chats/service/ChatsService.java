package kosta.main.chats.service;

import jakarta.persistence.EntityNotFoundException;
import kosta.main.chatrooms.dto.ChatListResponseDTO;
import kosta.main.chatrooms.entity.ChatRoom;
import kosta.main.chatrooms.repository.ChatRoomsRepository;
import kosta.main.chats.dto.ChatMessageDTO;
import kosta.main.chats.dto.ChatMessageResponseDTO;
import kosta.main.chats.entity.Chat;
import kosta.main.chats.repository.ChatsRepository;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        .createAt(chat.getCreatedAt().toString())
        .isRead(chat.isRead())
        .build();
  }


}
