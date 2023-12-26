package kosta.main.comments.dto;

import kosta.main.comments.entity.Comment;
import kosta.main.communityposts.entity.CommunityPost;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

@Getter
@AllArgsConstructor
public class CommentDTO {
    public static final String BEFORE_SECOND = "방금전";
    public static final String BEFORE_MINUTE = "분전";
    public static final String BEFORE_DATE = "일전";
    public static final String BEFORE_MONTH = "개월전";
    public static final String BEFORE_YEAR = "년전";
    public static final String BEFORE_HOUR = "시간전";
    private Integer communityPostId;
    private Integer commentId;
    private String content;
    private String date;

    public CommentDTO(Comment comment) {
        this.communityPostId = comment.getCommunityPost().getCommunityPostId();
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.date = makeDate(comment);
    }

    public static String makeDate(Comment comment) {
        LocalDateTime createdAt = comment.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(createdAt, now);
        Period period = Period.between(createdAt.toLocalDate(), now.toLocalDate());

        if (period.getYears() > 0) {
            return period.getYears() + BEFORE_YEAR;
        } else if (period.getMonths() > 0) {
            return period.getMonths() + BEFORE_MONTH;
        } else if (period.getDays() > 0) {
            return period.getDays() + BEFORE_DATE;
        } else if (duration.toHours() > 0) {
            return duration.toHours() + BEFORE_HOUR;
        } else if (duration.toMinutes() > 0) {
            return duration.toMinutes() + BEFORE_MINUTE;
        } else {
            return BEFORE_SECOND;
        }
    }


}
