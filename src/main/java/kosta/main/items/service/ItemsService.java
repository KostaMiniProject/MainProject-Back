package kosta.main.items.service;


import kosta.main.items.entity.Item;
import kosta.main.items.entity.ItemDeleteDto;
import kosta.main.items.entity.ItemSaveDto;
import kosta.main.items.repository.ItemsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ItemsService {
  private final ItemsRepository itemsRepository;

  //  물건 생성
  public void addItem(ItemSaveDto itemSaveDto) {
//    1. Item 엔티티의 title, description, itemStatus, imageUrl의  SETTER를 추가한다.
    //builder패턴에 대해서 공부해보면 좋음

//    2. Controller에서 받아온 ItemSaveDto를 ItemsRepository에 save 메서드를 이용하여 Item 테이블에 추가한다.
//    반환 타입 void
  }

  //  물건 목록 조회
  public List<Item> getItems() {
    List<Item> allItems = itemsRepository.findAll();
    return allItems;
  }

  //  물건 상세 조회
  public Item getFindById(int itemId) {
//    error
    return itemsRepository.findById(itemId).orElseThrow(() -> new RuntimeException("아이디를 찾지 못했습니다."));
  }

  //  물건 수정
  public void updateItem() {

//  1. Controller에서 받아온 itemId와 ItemSaveDto를 받아온다.
//  2. ItemsRepository에 findById 메서드를 이용하여 Item 테이블 내에 itemId를 찾는다.
//  3. 해당항목을 확인 후
//  title, description, itemStatus, imageUrl
//  4. "수정 완료" 문자열을 리턴한다.
//item.setItemStatus();
//
  }


  //  물건 삭제
    public void deleteItem(Integer itemId) {
//    1. Controller에서 받아온 ItemDeleteDto 중 itemId를 찾는다.
//    2. ItemsRepository에 findById 메서드를 이용하여 Item 테이블 내에 itemId를 찾는다.
//    3. Item 엔티티는 itemStatus의 SETTER를 추가한다.
//    4. Item 엔티티 itemId의 itemStatus를 DELETED로 변경한다.

      Item item = itemsRepository.findById(itemId).orElseThrow(() -> new RuntimeException("아이템을 찾을 수 없습니다."));
      item.setItemStatus(Item.ItemStatus.DELETED);
    }



  //  물건 검색
  //  ex - /items/search?name=메롱!
//  public Item searchItems() {
//    return null;
//  }
}


