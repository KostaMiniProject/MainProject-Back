package kosta.main.blockedusers.entity;
import jakarta.persistence.*;
import kosta.main.audit.Auditable;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "blocked_users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BlockedUser extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer blockedUserId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "blocking_user_id")
    private User blockingUser;


    // 게터와 세터
    // 생략...
}
