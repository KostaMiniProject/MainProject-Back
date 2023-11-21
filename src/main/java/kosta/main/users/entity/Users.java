package kosta.main.users.entity;
import jakarta.persistence.*;
import kosta.main.blockedusers.entity.BlockedUsers;

import java.util.List;
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
    private String status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BlockedUsers> blockedUsers; // 클래스 이름을 단수형으로 변경
    // 게터와 세터
    // 생략...
}
