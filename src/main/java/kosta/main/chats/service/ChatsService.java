package kosta.main.chats.service;

import kosta.main.chatrooms.repository.ChatRoomsRepository;
import kosta.main.chats.dto.ChatMessageDTO;
import kosta.main.chats.entity.Chat;
import kosta.main.chats.repository.ChatsRepository;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ChatsService {

  private final ChatRoomsRepository chatRoomsRepository;
  private final ChatsRepository chatsRepository;
  private final UsersRepository usersRepository;

  public ChatMessageDTO saveChat(ChatMessageDTO chatMessage, User user) {
    Chat chat = new Chat();
    chat.updateMessage(chatMessage.getContent());
    chat.updateUser(user);
    chatsRepository.save(chat);
    return chatMessage;
  }

}
