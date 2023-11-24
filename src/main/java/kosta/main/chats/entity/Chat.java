package kosta.main.chats.entity;
import jakarta.persistence.*;
import kosta.main.global.audit.Auditable;
import kosta.main.chatrooms.entity.ChatRoom;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chats")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Chat extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chatId;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column
    private String chatImage;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    // 게터와 세터
    // 생략...
}
