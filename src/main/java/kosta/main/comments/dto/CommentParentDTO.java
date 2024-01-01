package kosta.main.comments.dto;

import kosta.main.comments.entity.Comment;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.users.dto.response.UserCommentResponseDTO;
import kosta.main.users.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CommentParentDTO{
    public static final String BEFORE_SECOND = "방금전";
    public static final String BEFORE_MINUTE = "분전";
    public static final String BEFORE_DATE = "일전";
    public static final String BEFORE_MONTH = "개월전";
    public static final String BEFORE_YEAR = "년전";
    public static final String BEFORE_HOUR = "시간전";
    private Integer commentId;

    private String content;
    private Boolean isOwner;

    private UserCommentResponseDTO profile;
    private String date;



    private List<CommentChildDTO> children = new ArrayList<>();

    public CommentParentDTO(Integer commentId, String content,Boolean isOwner,UserCommentResponseDTO profile,String date) {
        this.commentId = commentId;
        this.content = content;
        this.isOwner = isOwner;
        this.profile = profile;
        this.date = date;
    }

    public static CommentParentDTO from(Comment comment, Integer userId){
        if(comment.getCommentStatus() != Comment.CommentStatus.DELETED) {
           return new CommentParentDTO(comment.getCommentId(), comment.getContent(), Objects.equals(userId, comment.getUser().getUserId()), UserCommentResponseDTO.from(comment.getUser()), makeDate(comment.getCreatedAt()));
        } else {
            return new CommentParentDTO(comment.getCommentId(), "삭제된 댓글입니다.", false, UserCommentResponseDTO.from(comment.getUser()),makeDate(comment.getCreatedAt()));
        }
    }

    public void addChild(CommentChildDTO childDTO){
        this.children.add(childDTO);
    }


    public static String makeDate(LocalDateTime createdAt) {
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
