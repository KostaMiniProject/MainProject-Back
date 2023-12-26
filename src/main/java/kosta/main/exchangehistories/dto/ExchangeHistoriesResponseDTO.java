package kosta.main.exchangehistories.dto;

import jakarta.persistence.Embeddable;
import kosta.main.items.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Getter
@AllArgsConstructor
public class ExchangeHistoriesResponseDTO {
    public static final String BEFORE_SECOND = "방금전";
    public static final String BEFORE_MINUTE = "분전";
    public static final String BEFORE_DATE = "일전";
    public static final String BEFORE_MONTH = "개월전";
    public static final String BEFORE_YEAR = "년전";
    public static final String BEFORE_HOUR = "시간전";
    private String createdAt; // 2023-11-21 화요일 형태로 제공해줘야함
    private Integer exchangePostId;
    private Integer reviewedUserId;
    private String reviewedUserName;
    private Boolean isWriteReview;
    private String posterName; // 나의 닉네임
    private String posterAddress; // 나의 주소
    private String posterProfileImage; // 나의 프로필 이미지
    private List<ItemDetailsDTO> posterItem; // 내가 건내준 물건의 리스트 (물건 이름, 물건 상세내용, 물건 대표이미지)
    private String bidderName; // 상대방 닉네임
    private String bidderAddress; // 상대방 사용자의 주소
    private String bidderProfileImage; // 상대방 유저의 프로필 이미지
    private List<ItemHistoryDTO> bidderItem; // 상대방이 건내준 물건의 리스트 (물건 이름, 물건 상세내용, 물건 대표이미지)
    @Getter
    @AllArgsConstructor
    public static class ItemDetailsDTO {
        private Integer itemId;
        private String title;
        private String description;
        private String imageUrl; // 첫 번째 이미지 URL만 저장
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class ItemHistoryDTO {
        private String title;
        private String description;
        private List<String> imageUrl;
    }

    public static String makeDate(Item item) {
        LocalDateTime createdAt = item.getCreatedAt();
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
