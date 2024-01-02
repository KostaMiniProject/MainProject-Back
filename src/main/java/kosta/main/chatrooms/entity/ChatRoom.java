package kosta.main.chatrooms.entity;
import jakarta.persistence.*;
import kosta.main.bids.entity.Bid;
import kosta.main.global.audit.Auditable;
import kosta.main.chats.entity.Chat;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_rooms")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@SQLDelete(sql = "UPDATE chat_rooms SET status = 'DELETED' WHERE chat_room_id = ?")
@Where(clause = "status <> 'DELETED'")
public class ChatRoom extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Integer chatRoomId;

    @ManyToOne
    @JoinColumn(name = "exchange_post_id", nullable = false)
    private ExchangePost exchangePost;

    @ManyToOne
    @JoinColumn(name = "bid_id", nullable = false)
    private Bid bid; // Bid 엔터티에 대한 참조추가 (23.12.06)

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = true) // 채팅방 나가기 기능을 위해 Not Null이 가능하도록 변경
    private User sender; // 교환 게시글 작성자에 해당

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = true) // 채팅방 나가기 기능을 위해 Not Null이 가능하도록 변경
    private User receiver; // 입찰자에 해당

    @Builder.Default
    @OneToMany(mappedBy = "chatRoom")
    private List<Chat> chats = new ArrayList<>();

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatRoomStatus status = ChatRoomStatus.ACTIVE; // 기본 상태는 ACTIVE

    public void updateSender(User sender) {
        this.sender = sender; // 교환 게시글 작성자에 해당
    }
    public void updateReceiver(User receiver) {
        this.receiver = receiver; // 입찰자에 해당
    }
    public void updateExchangePost(ExchangePost exchangePost) {
        this.exchangePost = exchangePost;
    }

    public void updateBid(Bid bid) {
        this.bid = bid;
    }
    public static ChatRoom of(ExchangePost exchangePost, Bid bid, User sender, User receiver) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.updateExchangePost(exchangePost);
        chatRoom.updateBid(bid);
        chatRoom.updateSender(sender);
        chatRoom.updateReceiver(receiver);
        return chatRoom;
    }

    public void delete() {
        this.status = ChatRoomStatus.DELETED; // 채팅방을 삭제하는 메서드
    }

    public enum ChatRoomStatus {
        ACTIVE, DELETED // 채팅방의 상태
    }

}
