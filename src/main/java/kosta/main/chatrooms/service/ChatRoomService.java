package kosta.main.chatrooms.service;

import kosta.main.chatrooms.repository.ChatRoomsRepository;
import kosta.main.chats.repository.ChatsRepository;
import kosta.main.users.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class ChatRoomService {
  private final ChatRoomsRepository chatRoomsRepository;
  private final ChatsRepository chatsRepository;
  private final UsersRepository usersRepository;
}
