package kosta.main.dibs.dto;


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
