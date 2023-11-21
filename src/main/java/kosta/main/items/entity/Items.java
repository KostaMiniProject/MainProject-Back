package kosta.main.items.entity;

import jakarta.persistence.*;
import kosta.main.bids.entity.Bids;
import kosta.main.categories.entity.Categories;
import kosta.main.users.entity.Users;
import java.time.LocalDateTime;

@Entity
@Table(name = "items")
public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "bid_id")
    private Bids bid;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categories category;

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 20, nullable = false)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 게터와 세터
    // 생략...
}
