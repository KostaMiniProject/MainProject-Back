package kosta.main.users.entity;
import jakarta.persistence.*;
import kosta.main.audit.Auditable;
import kosta.main.blockedusers.entity.BlockedUser;
import kosta.main.users.dto.UserCreateDto;
import kosta.main.users.dto.UserUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
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
    private List<BlockedUser> blockedUsers = new ArrayList<>(); // 클래스 이름을 단수형으로 변경


    public void updateUser(UserUpdateDto userUpdateDto) {
    }

    public enum UserStatus{
        ACTIVATE, BANNED ,DELETED
    }
    // 게터와 세터
    // 생략...

    public static User createUser(UserCreateDto userCreateDto){
        return User.builder()
                .name(userCreateDto.getName())
                .password(userCreateDto.getPassword())
                .email(userCreateDto.getEmail())
                .phone(userCreateDto.getPhone())
                .address(userCreateDto.getAddress())
                .build();
    }
    public static User createUser(UserUpdateDto userUpdateDto){
        return User.builder()
                .password(userUpdateDto.getPassword())
                .name(userUpdateDto.getName())
                .phone(userUpdateDto.getPhone())
                .address(userUpdateDto.getAddress())
                .profileImage(userUpdateDto.getProfileImage())
                .userStatus(userUpdateDto.getUserStatus())
                .build();
    }

    public void deleteUser(){
        this.userStatus = UserStatus.DELETED;
    }

}
