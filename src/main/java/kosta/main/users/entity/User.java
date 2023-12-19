package kosta.main.users.entity;
import jakarta.persistence.*;
import kosta.main.bids.entity.Bid;
import kosta.main.chatrooms.entity.ChatRoom;
import kosta.main.dibs.entity.Dib;
import kosta.main.exchangehistories.entity.ExchangeHistory;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.global.audit.Auditable;
import kosta.main.blockedusers.entity.BlockedUser;
import kosta.main.items.entity.Item;
import kosta.main.users.dto.request.UserCreateDTO;
import kosta.main.users.dto.request.UserUpdateDTO;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@SQLDelete(sql = "UPDATE users SET user_status = 2 WHERE user_id = ?")
@Where(clause = "user_status = 'ACTIVATE'") //문제있을수도있음
public class User extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(unique = true, length = 40)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 20)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 20)
    private String phone;

    @Column
    private String profileImage; // 사용자의 프로필 이미지

    @Builder.Default
    @Column(length = 20, nullable = false)
    private UserStatus userStatus = UserStatus.ACTIVATE;

    @Builder.Default
    @Column(length = 20, nullable = false)
    private String roles = Role.ROLE_USER.getRole();

    @Builder.Default
    @Column(nullable = false)
    private Double rating = 3.0;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> items = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ExchangePost> exchangePosts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Bid> bids = new ArrayList<>();




    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BlockedUser> blockedUsers = new ArrayList<>(); // 클래스 이름을 단수형으로 변경


    // 교환 내역에 대한 관계를 두 개의 별도 필드로 정의
    @OneToMany(mappedBy = "exchangeInitiator")
    private List<ExchangeHistory> initiatedExchanges;

    @OneToMany(mappedBy = "exchangePartner")
    private List<ExchangeHistory> participatedExchanges;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Dib> dibs = new ArrayList<>();


    public User updateUser(UserUpdateDTO userUpdateDto) {
        this.userStatus = !nullCheck(userUpdateDto.getUserStatus()) ? userUpdateDto.getUserStatus(): this.userStatus;
        this.address = !nullCheck(userUpdateDto.getAddress()) ? userUpdateDto.getAddress(): this.address;
        this.phone = !nullCheck(userUpdateDto.getPhone()) ? userUpdateDto.getPhone() : this.phone;
        this.password = !nullCheck(userUpdateDto.getPassword()) ? userUpdateDto.getPassword() : this.password;
        this.profileImage = !nullCheck(userUpdateDto.getProfileImage()) ? userUpdateDto.getProfileImage() : this.profileImage;
        this.name = !nullCheck(userUpdateDto.getName()) ? userUpdateDto.getName() : this.name;
        return this;
    }

    public void updatePassword(String password){
        this.password = password;
    }

    private boolean nullCheck(UserStatus userStatus) {
        return userStatus == null;
    }
    private boolean nullCheck(String string) {
        return string == null;
    }

    public enum UserStatus{
        ACTIVATE, BANNED ,DELETED
    }

    // 게터와 세터
    // 생략...

    public static User createUser(UserCreateDTO userCreateDto, String profileImage){
        return User.builder()
                .name(userCreateDto.getName())
                .password(userCreateDto.getPassword())
                .email(userCreateDto.getEmail())
                .phone(userCreateDto.getPhone())
                .address(userCreateDto.getAddress())
                .profileImage(profileImage)
                .build();
    }

    public void addBlockedUser(BlockedUser blockedUser){
        this.blockedUsers.add(blockedUser);
    }
}
