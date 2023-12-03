package kosta.main.exchangeposts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto {
    private Integer exchangePostId;

    public static ResponseDto of(Integer exchangePostId){
        return new ResponseDto(exchangePostId);
    }
}
