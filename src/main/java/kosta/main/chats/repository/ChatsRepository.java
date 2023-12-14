package kosta.main.chats.repository;

import kosta.main.chatrooms.entity.ChatRoom;
import kosta.main.chats.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatsRepository extends JpaRepository<Chat, Integer> {
  Page<Chat> findByChatRoom(ChatRoom chatRoom, Pageable pageable);
  Optional<Chat> findByChatId(Integer chatId);


}
