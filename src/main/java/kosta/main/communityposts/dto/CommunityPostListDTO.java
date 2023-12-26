package kosta.main.communityposts.dto;

import kosta.main.comments.dto.CommentParentDTO;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.likes.entity.Like;
import kosta.main.users.dto.response.UserItemResponseDTO;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@AllArgsConstructor
@Builder
public class CommunityPostListDTO { // stackoverflow에러로 인하여 조회 Dto 처리
    public static final String BEFORE_SECOND = "방금전";
    public static final String BEFORE_MINUTE = "분전";
    public static final String BEFORE_DATE = "일전";
    public static final String BEFORE_MONTH = "개월전";
    public static final String BEFORE_YEAR = "년전";
    public static final String BEFORE_HOUR = "시간전";
    private Integer communityPostId;

    private Boolean postOwner;

    private String title;
    private String content;

    private UserItemResponseDTO user;
    private List<String> imageUrl;
    private CommunityPost.CommunityPostStatus communityPostStatus;
    private Boolean isPressLike;
    private Integer commentCount;

    private Integer likeCount;
    private String date;

    public static CommunityPostListDTO from(CommunityPost communityPost, User user){
        return CommunityPostListDTO.builder()
                .communityPostId(communityPost.getCommunityPostId())
                .postOwner(user != null && communityPost.getUser().getUserId().equals(user.getUserId()))
                .title(communityPost.getTitle())
                .content(communityPost.getContent())
                .user(UserItemResponseDTO.of(communityPost.getUser()))
                .imageUrl(communityPost.getImages())
                .communityPostStatus(communityPost.getCommunityPostStatus())
                .isPressLike(checkPressLike(user,communityPost))
                .likeCount(communityPost.getLikePostList().size())
                .commentCount(communityPost.getCommentList().size())
                .date(makeDate(communityPost))
                .build();
    }

    public static boolean checkPressLike(User currentUser, CommunityPost post) {
        if (currentUser != null){
            Optional<Like> first = post
                    .getLikePostList()
                    .stream()
                    .filter(a -> Objects.equals(a.getUser().getUserId(), currentUser.getUserId()) && Objects.equals(a.getCommunityPost().getCommunityPostId(), post.getCommunityPostId()))
                    .findFirst();
            return first.isPresent();
        } else return false;
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