package kosta.main.chats.entity;
import jakarta.persistence.*;
import kosta.main.chatrooms.entity.ChatRooms;
import kosta.main.users.entity.Users;
import java.time.LocalDateTime;

@Entity
@Table(name = "chats")
public class Chats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "chatroom_id", nullable = false)
    private ChatRooms chatRoom;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column
    private String chatImage;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 게터와 세터
    // 생략...
}
