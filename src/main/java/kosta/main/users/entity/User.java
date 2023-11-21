package kosta.main.users.entity;
import jakarta.persistence.*;
import kosta.main.audit.Auditable;
import kosta.main.blockedusers.entity.BlockedUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(unique = true, length = 255)
    private String email;

    @Column(length = 255)
    private String password;

    @Column(length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String profileImage;

    @Column(length = 20, nullable = false)
    private UserStatus userStatus;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BlockedUser> blockedUsers; // 클래스 이름을 단수형으로 변경

    public enum UserStatus{
        ACTIVATE, BANNED ,DELETED
    }
    // 게터와 세터
    // 생략...
}
