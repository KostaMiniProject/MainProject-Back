package kosta.main.items.service;


import kosta.main.items.entity.Item;
import kosta.main.items.dto.ItemDto;
import kosta.main.items.repository.ItemsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ItemsService {
  private final ItemsRepository itemsRepository;


  //  물건 생성
  public void addItem(ItemDto itemDto) {
    Item newItem = new Item();
    newItem.setTitle(itemDto.getTitle());
    newItem.setDescription(itemDto.getDescription());
    newItem.setImageUrl(itemDto.getImageUrl());

    //    TODO : userId를 받아와서 newItem에 추가
    //    TODO : bidId를 받아와서 newItem에 추가
    //    TODO : categoryId를 받아와서 newItem에 추가

    itemsRepository.save(newItem);
  }


  //  물건 목록 조회
  public List<Item> getItems() {
    return itemsRepository.findAll();
  }


  //  물건 상세 조회
  public Item getFindById(int itemId) {
    return itemsRepository.findById(itemId).orElseThrow(() -> new RuntimeException("아이디를 찾지 못했습니다."));
  }


  //  물건 수정
  public void updateItem(Integer itemId, ItemDto itemDto) {
    Item updateItem = getFindById(itemId);

    if (itemDto.getTitle() != null) {
      updateItem.setTitle(itemDto.getTitle());
    }
    if (itemDto.getDescription() != null) {
      updateItem.setDescription(itemDto.getDescription());
    }
    if (itemDto.getImageUrl() != null) {
      updateItem.setImageUrl(itemDto.getImageUrl());
    }
    if (itemDto.getItemStatus() != null) {
      updateItem.setItemStatus(itemDto.getItemStatus());
    }
    itemsRepository.save(updateItem);
  }


  //  물건 삭제
  public void deleteItem(Integer itemId) {
    Item item = getFindById(itemId);
    item.setItemStatus(Item.ItemStatus.DELETED);
  }


  //  물건 검색
  //  ex - /items/search?name=메롱!
//  public Item searchItems() {
//    return null;
//  }
}


