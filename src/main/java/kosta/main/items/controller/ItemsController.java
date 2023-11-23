package kosta.main.items.controller;

import kosta.main.items.entity.Item;
import kosta.main.items.entity.ItemSaveDto;
import kosta.main.items.entity.ItemUpdateDto;
import kosta.main.items.service.ItemsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemsController {
  private final ItemsService itemsService;

  //  물건 생성
  @PostMapping
  public void addItem(@RequestBody ItemSaveDto itemSaveDto) {
    itemsService.addItem(itemSaveDto);
  }

  //  물건 목록 조회
  @GetMapping
  public List<Item> getItems() {
    List<Item> allItems = itemsService.getItems();
    return allItems;
  }

  //  물건 상세 조회
  @GetMapping("/{itemId}")
  public Item getFindById(@PathVariable int itemId) {
    return itemsService.getFindById(itemId);
  }

  //  물건 수정
  @PutMapping("/{itemId}")
  public void updateItem(@PathVariable int itemId,
                         @RequestBody ItemUpdateDto itemUpdateDto) {
    itemsService.updateItem(itemId, itemUpdateDto);
  }


  //  물건 삭제
  @DeleteMapping("/{itemId}")
  public void deleteItem(@PathVariable Integer itemId) {
    itemsService.deleteItem(itemId);
  }

  //  물건 검색
  //  ex - /items/search?name=제목!!!
//  @GetMapping("/search")
//  public Item searchItems() {
//    return null;
//  }
}
