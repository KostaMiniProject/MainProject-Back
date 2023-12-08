package kosta.main.global.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
@AllArgsConstructor
public class PageInfo {

    private int page;
    private int size;
    private int totalElements;
    private int totalPages;
    public static PageInfo of(Page<?> page){
        return new PageInfo(page.getNumber()+1, page.getSize(), (int) page.getTotalElements(), page.getTotalPages());
    }
}
