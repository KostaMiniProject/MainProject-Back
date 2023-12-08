package kosta.main.chats.repository;

import kosta.main.chats.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatsRepository extends JpaRepository<Chat, Integer> {
}
