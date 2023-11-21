package kosta.main.chatrooms.entity;
import jakarta.persistence.*;
import kosta.main.chats.entity.Chats;
import kosta.main.exchangeposts.entity.ExchangePosts;
import kosta.main.users.entity.Users;
import java.util.List;

@Entity
@Table(name = "chat_rooms")
public class ChatRooms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "exchange_post_id", nullable = false)
    private ExchangePosts exchangePost;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Users sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private Users receiver;

    @OneToMany(mappedBy = "chatRoom")
    private List<Chats> chats;

    // 게터와 세터
    // 생략...
}
