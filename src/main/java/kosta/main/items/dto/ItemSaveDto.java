package kosta.main.items.dto;

import kosta.main.items.entity.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemSaveDto {
  //private Integer userId; // 아이템을 등록하는 user의 ID
  //private Integer categoryId; // 카테고리는 나중에 데이터 삽입후에 차차 진행
  private String title; // 아이템 제목
  private String category; // 카테고리
  private String description; // 아이템 설명
  private Item.ItemStatus itemStatus; // 아이템 상태
}
