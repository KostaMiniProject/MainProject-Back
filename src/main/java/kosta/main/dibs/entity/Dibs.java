package kosta.main.dibs.entity;
import jakarta.persistence.*;
import kosta.main.exchangeposts.entity.ExchangePosts;
import kosta.main.users.entity.Users;

@Entity
@Table(name = "dibs")
public class Dibs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "exchange_post_id")
    private ExchangePosts exchangePost;

    // 게터와 세터
    // 생략...
}
