package kosta.main.bids.entity;
import jakarta.persistence.*;
import kosta.main.exchangeposts.entity.ExchangePosts;
import kosta.main.users.entity.Users;

import java.time.LocalDateTime;

@Entity
@Table(name = "bids")
public class Bids {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "exchange_post_id")
    private ExchangePosts exchangePost;

    @Column(length = 20, nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 게터와 세터
    // 생략...
}
