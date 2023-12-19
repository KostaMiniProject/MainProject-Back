package kosta.main.communityposts.dto;

import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.likes.entity.Like;
import kosta.main.users.dto.response.UserItemResponseDTO;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Builder
public class CommunityPostDetailDTO {
    public static final String BEFORE_SECOND = "초전";
    public static final String BEFORE_MINUTE = "분전";
    public static final String BEFORE_DATE = "일전";
    public static final String BEFORE_MONTH = "개월전";
    public static final String BEFORE_YEAR = "년전";
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


    public static CommunityPostDetailDTO from(CommunityPost communityPost, User user){
        return CommunityPostDetailDTO.builder()
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
                    .filter(a -> Objects.equals(a.getUser().getUserId(), currentUser.getUserId()))
                    .findFirst();
            return first.isPresent();
        } else return false;
    }

    public static String makeDate(CommunityPost post) {
        LocalDateTime createdAt = post.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();
        if (createdAt.isBefore(now.minusMonths(1))) {
            if (createdAt.isBefore(now.minusDays(1))) {
                if (createdAt.isBefore(now.minusHours(1))) {
                    if (createdAt.isBefore(now.minusMinutes(1))) {
                        return (now.getSecond() - createdAt.getSecond()) + BEFORE_SECOND;
                    }
                    return (now.getMinute() - createdAt.getMinute()) + BEFORE_MINUTE;
                }
                return (now.getDayOfMonth() - createdAt.getDayOfMonth()) + BEFORE_DATE;
            }
            return (now.getMonthValue() - createdAt.getMonthValue()) + BEFORE_MONTH;
        } else return (now.getYear() - createdAt.getYear()) + BEFORE_YEAR;
    }

}
