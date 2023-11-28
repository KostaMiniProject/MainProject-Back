package kosta.main.chats.entity;
import jakarta.persistence.*;
import kosta.main.global.audit.Auditable;
import kosta.main.chatrooms.entity.ChatRoom;
import kosta.main.images.entity.Image;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "imageId")
    private Image chatImage; // 채팅에 첨부되는 이미지

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private boolean isRead = false; // 채팅 메시지 읽음 상태


    // 게터와 세터
    // 생략...
}
