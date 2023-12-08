package kosta.main.chats.entity;
import jakarta.persistence.*;
import kosta.main.bids.entity.Bid;
import kosta.main.global.audit.Auditable;
import kosta.main.chatrooms.entity.ChatRoom;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chats")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
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
    private String chatImage; // 채팅에 첨부되는 이미지

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Column
    @Builder.Default
    private boolean isRead = false; // 채팅 메시지 읽음 상태

    public void updateMessage(String message) {
        this.message = message;
    }
    public void updateUser(User user) {
        this.user = user;
    }
    public void updateChatRoom(ChatRoom chatRoom){
        this.chatRoom = chatRoom;
    }

    public Boolean updateIsRead(Boolean isRead){return this.isRead = isRead;}

}
