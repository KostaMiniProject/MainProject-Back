package kosta.main.dibs.dto;

import kosta.main.dibs.entity.Dib;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DibbedExchangePostDTO {
    private Integer exchangePostId;
    private String title;
    private String representativeImageUrl;
    private LocalDateTime createdAt;
}
