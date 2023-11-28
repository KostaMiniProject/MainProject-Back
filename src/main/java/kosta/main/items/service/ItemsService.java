package kosta.main.items.service;


import kosta.main.bids.entity.Bid;
import kosta.main.categories.entity.Category;
import kosta.main.items.dto.ItemUpdateDto;
import kosta.main.items.entity.Item;
import kosta.main.items.dto.ItemSaveDto;
import kosta.main.items.repository.ItemsRepository;
import kosta.main.users.entity.User;
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
//    # sudo 코드
//    1. Controller에서 ItemSaveDto값을 받아온다.
//    2. 추가할 내용을 담는 용도로 Item 객체(newItem)를 생성한다.
//    3. ItemSaveDto 내에서 getter을 이용해 추가하고 싶은 값을 꺼낸다.
//    4. 꺼낸값을 newItem 객체의 setter를 통해 값을 넣어준다.
//    5. 3번 4번 과정을 반복한다.
//      (user, bid, category, title, description, imageUrl)
//    6. itemsRepository의 save 메서드를 이용해서 newItem을 DB에 적용시켜준다.

//    # SETTER 사용
//    Item newItem1 = new Item();
//    newItem1.setTitle(itemSaveDto.getTitle());
//    newItem1.setDescription(itemSaveDto.getDescription());
//    newItem1.setImageUrl(itemSaveDto.getImageUrl());

//    # Builder 사용
    Item newItem2 = Item.builder()
        //    TODO : userId를 받아와서 추가
        .user(new User())
        //    TODO : bidId를 받아와서 추가
        .bid(new Bid())
        //    TODO : categoryId를 받아와서 추가
        .category(new Category())
        .title(itemSaveDto.getTitle())
        .description(itemSaveDto.getDescription())
        //.imageUrl(itemSaveDto.getImageUrl())
        .build();

    itemsRepository.save(newItem2);
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
//    # sudo 코드
//    1. Controller에서 itemId와 ItemUpdateDto값을 받아온다.
//    2. 수정할 내용을 담는 용도인 Item 객체(updateItem)를 생성한다.
//    3. itemRepository의 getFindById 메서드와 itemId를 이용해 updateItem 객체를 초기화한다.
//    4. itemUpdateDto에 포함된 요소를 한개씩 getter를 통해서 꺼내 null인지 확인한다.
//    5. 만약 null이 아니라면 updateItem 객체의 setter를 통해 값을 넣어준다.
//    6. 4번 5번 과정을 반복한다.
//      (title, description, imageUrl, itemStatus)
//    7. itemsRepository의 save 메서드를 이용해서 updateItem을 DB에 적용시켜준다.

//    # SETTER 사용
//    Item updateItem1 = getFindById(itemId);
//
//    if (itemUpdateDto.getTitle() != null) {
//      updateItem1.setTitle(itemUpdateDto.getTitle());
//    }
//    if (itemUpdateDto.getDescription() != null) {
//      updateItem1.setDescription(itemUpdateDto.getDescription());
//    }
//    if (itemUpdateDto.getImageUrl() != null) {
//      updateItem1.setImageUrl(itemUpdateDto.getImageUrl());
//    }
//    if (itemUpdateDto.getItemStatus() != null) {
//      updateItem1.setItemStatus(itemUpdateDto.getItemStatus());
//    }
    
    
//    # Builder 사용
    Item item = getFindById(itemId);
    
//    itemUpdateDto 요소 null값 체크
    String title = itemUpdateDto.getTitle() != null ? itemUpdateDto.getTitle() : item.getTitle();
    String description = itemUpdateDto.getDescription() != null ? itemUpdateDto.getDescription() : item.getDescription();
   // String imageUrl = itemUpdateDto.getImageUrl() != null ? itemUpdateDto.getImageUrl() : item.getImageUrl();
    Item.ItemStatus itemStatus = itemUpdateDto.getItemStatus() != null ? itemUpdateDto.getItemStatus() : item.getItemStatus();

    Item updateItem2 = Item.builder()
        .itemId(itemId)
        .title(title)
        .description(description)
        //.imageUrl(imageUrl)
        .itemStatus(itemStatus)
        .build();

    itemsRepository.save(updateItem2);
  }


  //  물건 삭제
  public void deleteItem(Integer itemId) {
    Item item = getFindById(itemId);
    item.itemStatusUpdate(Item.ItemStatus.DELETED);
  }

  //  물건 검색
  //  ex - /items/search?name=메롱!
//  public Item searchItems() {
//    return null;
//  }
}


