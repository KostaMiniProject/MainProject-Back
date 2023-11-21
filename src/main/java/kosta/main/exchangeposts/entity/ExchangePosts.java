package kosta.main.exchangeposts.entity;

import jakarta.persistence.*;
import kosta.main.items.entity.Items;
import kosta.main.users.entity.Users;

import java.time.LocalDateTime;

@Entity
@Table(name = "exchange_posts")
public class ExchangePosts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Items item;

    @Column(length = 255)
    private String title;

    @Column(length = 100)
    private String preferItems;

    @Column(length = 255)
    private String address;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 20, nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 게터와 세터
    // 생략...
}
