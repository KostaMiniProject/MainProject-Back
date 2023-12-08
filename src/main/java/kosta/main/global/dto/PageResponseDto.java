package kosta.main.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class PageResponseDto<T> {

    private T data;
    private PageInfo pageInfo;

}
