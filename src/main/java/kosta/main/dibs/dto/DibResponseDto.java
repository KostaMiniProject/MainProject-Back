package kosta.main.dibs.dto;

import kosta.main.dibs.entity.Dib;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DibResponseDto {
    private User user;
    private ExchangePost exchangePost;

    public static DibResponseDto of(Dib dib){
        return new DibResponseDto(dib.getUser(), dib.getExchangePost());
    }
}
