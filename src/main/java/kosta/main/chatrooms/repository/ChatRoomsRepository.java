package kosta.main.chatrooms.repository;

import kosta.main.chatrooms.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomsRepository extends JpaRepository<ChatRoom, Integer> {
}
