package kosta.main.blockedusers.entity;
import jakarta.persistence.*;
import kosta.main.users.entity.Users;
import java.time.LocalDateTime;

@Entity
@Table(name = "blockedusers")
public class BlockedUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "blocked_user_id")
    private Users blockedUser;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 게터와 세터
    // 생략...
}
