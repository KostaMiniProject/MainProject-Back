package kosta.main.chats.service;

import jakarta.persistence.EntityNotFoundException;
import kosta.main.chatrooms.entity.ChatRoom;
import kosta.main.chatrooms.repository.ChatRoomsRepository;
import kosta.main.chats.dto.ChatMessageDTO;
import kosta.main.chats.entity.Chat;
import kosta.main.chats.repository.ChatsRepository;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ChatsService {

  private final ChatRoomsRepository chatRoomsRepository;
  private final ChatsRepository chatsRepository;
  private final UsersRepository usersRepository;

  public ChatMessageDTO saveChat(ChatMessageDTO chatMessage) {
    ChatRoom chatRoom = chatRoomsRepository.findById(chatMessage.getChatRoomId())
        .orElseThrow(() -> new RuntimeException("ChatRoom not found"));
    User sender = usersRepository.findById(chatMessage.getSenderId())
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + chatMessage.getSenderId()));
    Chat chat = new Chat();
    System.out.printf("보낸 사람의 ID : %d, 보낸 내용은 %s \n",chatMessage.getSenderId(),chatMessage.getContent());
    chat.updateMessage(chatMessage.getContent());
    chat.updateUser(sender);
    chat.updateChatRoom(chatRoom);
    chatsRepository.save(chat);
    return chatMessage;
  }

}
