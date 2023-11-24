package kosta.main.chatrooms.entity;
import jakarta.persistence.*;
import kosta.main.global.audit.Auditable;
import kosta.main.chats.entity.Chat;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Entity
@Table(name = "chat_rooms")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatRoom extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Integer chatRoomId;

    @ManyToOne
    @JoinColumn(name = "exchange_post_id", nullable = false)
    private ExchangePost exchangePost;

    @OneToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @OneToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @OneToMany(mappedBy = "chatRoom")
    private ArrayList<Chat> chats = new ArrayList<>();

    // 게터와 세터
    // 생략...
}
