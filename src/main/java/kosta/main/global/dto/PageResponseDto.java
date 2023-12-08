package kosta.main.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

@Getter
@AllArgsConstructor
public class PageResponseDto<T> {

    private T data;
    private PageInfo pageInfo;

}
