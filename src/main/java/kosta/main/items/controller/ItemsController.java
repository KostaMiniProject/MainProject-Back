package kosta.main.items.controller;

import kosta.main.items.dto.ItemUpdateDto;
import kosta.main.items.entity.Item;
import kosta.main.items.dto.ItemSaveDto;
import kosta.main.items.service.ItemsService;
import kosta.main.users.entity.LoginUser;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/items")
@AllArgsConstructor
public class ItemsController {
  private final ItemsService itemsService;

  //  물건 생성
  @PostMapping
  public ResponseEntity<?> addItem(@LoginUser User user, @RequestPart("itemSaveDto") ItemSaveDto itemSaveDto,
                                @RequestPart(value = "file") List<MultipartFile> files) {
     itemsService.addItem(user,itemSaveDto, files);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  //  물건 목록 조회
  @GetMapping
  public ResponseEntity<?> getItems() {
    List<Item> allItems = itemsService.getItems();
    return new ResponseEntity<>(allItems,HttpStatus.OK);
  }

  //  물건 상세 조회
  @GetMapping("/{itemId}")
  public ResponseEntity<?> getFindById(@PathVariable int itemId) {
    Item findById = itemsService.getFindById(itemId);
    return new ResponseEntity<>(findById,HttpStatus.OK);
  }

  //  물건 수정
  @PutMapping("/{itemId}")
  public ResponseEntity<?> updateItem(@PathVariable int itemId,
                         @RequestPart("itemUpdateDto") ItemUpdateDto itemUpdateDto,
                         @RequestPart(value = "file") List<MultipartFile> files) {
    Item item = itemsService.updateItem(itemId, itemUpdateDto, files);
    return new ResponseEntity<>(item, HttpStatus.OK);
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
