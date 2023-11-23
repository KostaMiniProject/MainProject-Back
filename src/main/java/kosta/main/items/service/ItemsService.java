package kosta.main.items.service;


import kosta.main.items.entity.Item;
import kosta.main.items.entity.ItemSaveDto;
import kosta.main.items.entity.ItemUpdateDto;
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
  public void addItem(ItemSaveDto itemSaveDto) {
    Item newItem = new Item();
    newItem.setTitle(itemSaveDto.getTitle());
    newItem.setDescription(itemSaveDto.getDescription());
    newItem.setImageUrl(itemSaveDto.getImageUrl());

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
  public void updateItem(Integer itemId, ItemUpdateDto itemUpdateDto) {
    Item updateItem = getFindById(itemId);

    if (itemUpdateDto.getTitle() != null) {
      updateItem.setTitle(itemUpdateDto.getTitle());
    }
    if (itemUpdateDto.getDescription() != null) {
      updateItem.setDescription(itemUpdateDto.getDescription());
    }
    if (itemUpdateDto.getImageUrl() != null) {
      updateItem.setImageUrl(itemUpdateDto.getImageUrl());
    }
    if (itemUpdateDto.getItemStatus() != null) {
      updateItem.setItemStatus(itemUpdateDto.getItemStatus());
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


