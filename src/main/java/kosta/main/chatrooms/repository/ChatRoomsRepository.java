package kosta.main.chatrooms.repository;

import kosta.main.chatrooms.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomsRepository extends JpaRepository<ChatRoom, Integer> {

  List<ChatRoom> findBySenderUserIdOrReceiverUserId(Integer senderId, Integer receiverId);
  @Query(value = "SELECT * FROM chat_rooms c WHERE c.sender_id = :senderId OR c.receiver_id = :receiverId", nativeQuery = true)
  List<ChatRoom> findBySenderUserId(Integer senderId, Integer receiverId);

  Optional<ChatRoom> findByBid_BidId(Integer bidId);

}
