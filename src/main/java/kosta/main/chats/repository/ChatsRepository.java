package kosta.main.chats.repository;

import kosta.main.chatrooms.entity.ChatRoom;
import kosta.main.chats.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatsRepository extends JpaRepository<Chat, Integer> {
  List<Chat> findByChatRoom(ChatRoom chatRoom);

}
