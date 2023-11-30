package kosta.main.dibs.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DibRequestDto {
    private Integer userId; // 찜을 누르는 사용자의 ID
    private Integer exchangePostId; // 찜을 누르는 교환 게시글의 종류
}
