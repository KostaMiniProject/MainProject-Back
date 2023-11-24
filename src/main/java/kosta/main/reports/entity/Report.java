package kosta.main.reports.entity;

import jakarta.persistence.*;
import kosta.main.global.audit.Auditable;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reports")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
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
    @Builder.Default
    private ReportStatus result = ReportStatus.REPORTING;


    public enum ReportStatus{
        REPORTING,RECEIVED,APPLIED
    }
    // 게터와 세터
    // 생략...
}
