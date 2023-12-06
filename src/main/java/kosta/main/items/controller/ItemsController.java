package kosta.main.items.controller;

import kosta.main.items.dto.ItemPageDTO;
import kosta.main.items.dto.ItemDetailResponseDTO;
import kosta.main.items.dto.ItemUpdateDTO;
import kosta.main.items.dto.ItemSaveDTO;
import kosta.main.items.service.ItemsService;
import kosta.main.users.entity.LoginUser;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
  
  /**
   *  물건 생성
   * @param user
   * @param itemSaveDTO
   * @param files
   * @return
   */
  @PostMapping
  public ResponseEntity<?> addItem(@LoginUser User user, @RequestPart("itemSaveDTO") ItemSaveDTO itemSaveDTO,
                                @RequestPart(value = "file") List<MultipartFile> files) {
     itemsService.addItem(user,itemSaveDTO, files);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }


  /**
   * 물건 목록 조회
   * @param user
   * @param pageable
   * @return
   */
  // 23.12.04 해당하는 유저의 Item만 가져오도록 수정
  @GetMapping
  public ResponseEntity<?> getItems(@LoginUser User user,
                                    @PageableDefault(page = 0, size = 10, sort = "itemId", direction = Sort.Direction.DESC) Pageable pageable) {
    Page<ItemPageDTO> allItems = itemsService.getItems(user.getUserId(), pageable);
    return new ResponseEntity<>(allItems,HttpStatus.OK);
  }


  /**
   * 물건 상세 조회
   * @param itemId
   * @return
   */
  @GetMapping("/{itemId}")
  public ResponseEntity<?> getFindById(@PathVariable int itemId) {
    ItemDetailResponseDTO findById = itemsService.getFindById(itemId);
    return new ResponseEntity<>(findById,HttpStatus.OK);
  }


  /**
   * 물건 수정
   * @param itemId
   * @param itemUpdateDTO
   * @param files
   * @param user
   * @return
   */
  @PutMapping("/{itemId}")
  public ResponseEntity<?> updateItem(@PathVariable int itemId,
                         @RequestPart("itemUpdateDTO") ItemUpdateDTO itemUpdateDTO,
                         @RequestPart(value = "file") List<MultipartFile> files,
                                      @LoginUser User user) {
    return new ResponseEntity<>(itemsService.updateItem(itemId, itemUpdateDTO, files,user), HttpStatus.OK);
  }


  /**
   * 물건 삭제
   * @param itemId
   * @param user
   * @return
   */
  // 23.12.04 : 응답 데이터에 Status 추가
  @DeleteMapping("/{itemId}")
  public ResponseEntity<?> deleteItem(@PathVariable Integer itemId, @LoginUser User user) {
    itemsService.deleteItem(itemId, user.getUserId());
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }



  //  물건 검색
  //  ex - /items/search?name=제목!!!
//  @GetMapping("/search")
//  public Item searchItems() {
//    return null;
//  }

}
