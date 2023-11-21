package kosta.main.reports.entity;

import jakarta.persistence.*;
import kosta.main.audit.Auditable;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Report extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reportId;

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "reported_user_id")
    private User reportedUser;

    @ManyToOne
    @JoinColumn(name = "reported_community_post_id")
    private CommunityPost reportedPost;

    @ManyToOne
    @JoinColumn(name = "reported_exchange_post_id")
    private ExchangePost exchangePost;


    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(length = 20)
    private String result;

    // 게터와 세터
    // 생략...
}
