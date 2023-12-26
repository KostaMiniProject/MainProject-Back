package kosta.main.communityposts.dto;

import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.users.dto.response.UsersResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

@Getter
@AllArgsConstructor
public class CommunityPostResponseDTO {
    public static final String BEFORE_SECOND = "방금전";
    public static final String BEFORE_MINUTE = "분전";
    public static final String BEFORE_DATE = "일전";
    public static final String BEFORE_MONTH = "개월전";
    public static final String BEFORE_YEAR = "년전";
    public static final String BEFORE_HOUR = "시간전";
    private UsersResponseDTO user;

    private String title;

    private String content;

    private Integer views;
    private CommunityPost.CommunityPostStatus communityPostStatus;
    private Integer likeCount;
    private String date;

    public static CommunityPostResponseDTO of(CommunityPost communityPost){ //of : 데이터를 가지고 새로운 객체를 만들때 사용
        return new CommunityPostResponseDTO(
                UsersResponseDTO.of(communityPost.getUser()),
                communityPost.getTitle(),
                communityPost.getContent(),
                communityPost.getViews(),
                communityPost.getCommunityPostStatus(),
                communityPost.getLikePostList().size(),
                makeDate(communityPost)
        );
    }

    public static String makeDate(CommunityPost post) {
        LocalDateTime createdAt = post.getCreatedAt();
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
