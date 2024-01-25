package kosta.main.users.entity;
import jakarta.persistence.*;
import kosta.main.bids.entity.Bid;
import kosta.main.dibs.entity.Dib;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.global.audit.Auditable;
import kosta.main.blockedusers.entity.BlockedUser;
import kosta.main.items.entity.Item;
import kosta.main.reviews.entity.Review;
import kosta.main.users.auth.oauth2.dto.OauthSignUpDTO;
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
//@Where(clause = "user_status = 'ACTIVATE'") //문제있을수도있음
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
    @Builder.Default
    private String address = "OAuth2 User";

    @Column(length = 20)
    @Builder.Default
    private String phone = "OAuth2 User";

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
    @Column(nullable = false)
    private Boolean social =false;

    @Builder.Default
    @Column(nullable = false)
    private String provider = "OriginUser";

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
    @OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();




    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BlockedUser> blockedUsers = new ArrayList<>(); // 클래스 이름을 단수형으로 변경


    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Dib> dibs = new ArrayList<>();


    public User updateUser(UserUpdateDTO userUpdateDto) {
        this.userStatus = !nullCheck(userUpdateDto.getUserStatus()) ? userUpdateDto.getUserStatus(): this.userStatus;
        this.address = userUpdateDto.getAddress() != null ? userUpdateDto.getAddress().getRoadAddr() + " " +userUpdateDto.getAddressDetail(): this.address;
        this.phone = !nullCheck(userUpdateDto.getPhone()) ? userUpdateDto.getPhone() : this.phone;
        this.password = !nullCheck(userUpdateDto.getPassword()) ? userUpdateDto.getPassword() : this.password;
        this.profileImage = !nullCheck(userUpdateDto.getProfileImage()) ? userUpdateDto.getProfileImage() : this.profileImage;
        this.name = !nullCheck(userUpdateDto.getNickName()) ? userUpdateDto.getNickName() : this.name;
        return this;
    }
    public User updateUser(UserCreateDTO userCreateDTO) {
        this.address = userCreateDTO.getAddress() != null ? userCreateDTO.getAddress().getRoadAddr() + " " +userCreateDTO.getAddressDetail(): this.address;
        this.phone = !nullCheck(userCreateDTO.getPhone()) ? userCreateDTO.getPhone() : this.phone;
        this.password = !nullCheck(userCreateDTO.getPassword()) ? userCreateDTO.getPassword() : this.password;
        this.name = !nullCheck(userCreateDTO.getNickName()) ? userCreateDTO.getNickName() : this.name;
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

    public User oauthSignUp(OauthSignUpDTO oauthSignUpDTO) {
        this.address = oauthSignUpDTO.getAddress() != null ? oauthSignUpDTO.getAddress().getRoadAddr() + " " +oauthSignUpDTO.getAddressDetail(): this.address;
        this.phone = !nullCheck(oauthSignUpDTO.getPhone()) ? oauthSignUpDTO.getPhone() : this.phone;
        this.name = !nullCheck(oauthSignUpDTO.getNickName()) ? oauthSignUpDTO.getNickName() : this.name;
        return this;
    }

    public void updateStatus() {
        this.userStatus = UserStatus.ACTIVATE;
    }

    public enum UserStatus{
        ACTIVATE, BANNED ,DELETED
    }

    // 게터와 세터
    // 생략...

    public static User createUser(UserCreateDTO userCreateDto, String profileImage){
        return User.builder()
                .name(userCreateDto.getNickName())
                .password(userCreateDto.getPassword())
                .email(userCreateDto.getEmail())
                .phone(userCreateDto.getPhone())
                .address(userCreateDto.getAddress().getRoadAddr() + " " + userCreateDto.getAddressDetail())
                .profileImage(profileImage)
                .build();
    }

    public void addBlockedUser(BlockedUser blockedUser){
        this.blockedUsers.add(blockedUser);
    }
    public void removeBlockedUser(BlockedUser blockedUser){
        this.blockedUsers.remove(blockedUser);
    }
    public void updateReviews(Review review){
        this.reviews.add(review);
    }
    public void updateRating(Integer reviewPoint){
        Integer reviewSize = reviews.size() + 1; //기본값 3을 위한 +1
        this.rating = ((this.rating * reviewSize) + reviewPoint) / (reviewSize + 1); // 현재 rating에 reviews size만큼 수치를 더해줌 여기에 새로받은 포인트를 더해주고 size에 +1 한 값으로 나눔
    }
    public void updateProfileImage(String profileImage){
        this.profileImage = profileImage;
    }
}
