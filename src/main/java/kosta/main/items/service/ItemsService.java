package kosta.main.items.service;


import kosta.main.items.entity.Item;
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
//  public String addItem(Item item) {
//    Item addItem = itemsRepository.save(item);
//    log.info(String.valueOf(addItem));
//    return "물건 생성 완료";
//  }

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
//  public String updateItem(@PathVariable int itemId) {
//
//    return "수정 완료";
//  }


  //  물건 삭제
  public String deleteItem(int itemId) {
    itemsRepository.deleteById(itemId);
    return "삭제 완료";
  }

  //  물건 검색
  //  ex - /items/search?name=메롱!
//  public Item searchItems() {
//    return null;
//  }
}
