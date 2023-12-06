package kosta.main.items.service;

import kosta.main.categories.repository.CategoriesRepository;
import kosta.main.global.error.exception.BusinessException;
import kosta.main.global.error.exception.ErrorCode;
import kosta.main.global.s3upload.service.ImageService;
import kosta.main.items.dto.ItemDetailResponseDTO;
import kosta.main.items.dto.ItemUpdateDto;
import kosta.main.items.dto.ItemUpdateResponseDto;
import kosta.main.items.entity.Item;
import kosta.main.items.dto.ItemSaveDto;
import kosta.main.items.repository.ItemsRepository;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static kosta.main.global.error.exception.ErrorCode.ALREADY_BIDDING_ITEM;
import static kosta.main.global.error.exception.ErrorCode.NOT_ITEM_OWNER;

@Service
@Transactional
@AllArgsConstructor
public class ItemsService {

  private final ItemsRepository itemsRepository;
  private final UsersRepository usersRepository;
  private final CategoriesRepository categoriesRepository;
  private final ImageService imageService;



  //  물건 생성
  public void addItem(User user, ItemSaveDto itemSaveDto, List<MultipartFile> files) {
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

    // 사용자와 카테고리 정보 조회
//    Category category = categoriesRepository.findById(itemSaveDto.getCategoryId())
//            .orElseThrow(() -> new RuntimeException("Category not found"));

    List<String> imagePaths = files.stream().map(imageService::resizeToBasicSizeAndUpload).toList();

    // Item 객체 생성
    Item newItem = Item.builder()
            .user(user)
            //.category(category)
            .title(itemSaveDto.getTitle())
            .description(itemSaveDto.getDescription())
            .images(imagePaths)
            .build();

    // Item 저장
    itemsRepository.save(newItem);
  }


  //  물건 목록 조회
  public List<Item> getItems(Integer userId) {
    return itemsRepository.findByUser_UserId(userId);
  }


  //  물건 상세 조회
  public ItemDetailResponseDTO getFindById(int itemId) {
    Item itemByItemId = findItemByItemId(itemId);
    return ItemDetailResponseDTO.of(itemByItemId);
  }

  private Item findItemByItemId(int itemId) {
    return itemsRepository.findById(itemId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
  }


  //  물건 수정
  public ItemUpdateResponseDto updateItem(Integer itemId, ItemUpdateDto itemUpdateDto, List<MultipartFile> files, User user) {
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
    List<String> imagePath = new ArrayList<>(files.stream().map(imageService::resizeToBasicSizeAndUpload).toList());
    itemUpdateDto.updateImagePath(imagePath);
//    # Builder 사용
    Item item = findItemByItemId(itemId);

    // 사용자 ID 일치 여부 확인 (23.12.04)
    if (!item.getUser().getUserId().equals(user.getUserId())) {
      throw new BusinessException(NOT_ITEM_OWNER);
    }
    //사용중인 Item은 변경 불가(23.12.04)
    if (item.getIsBiding() == Item.IsBiding.BIDING) {
      throw new BusinessException(ALREADY_BIDDING_ITEM);
    }
    
//    itemUpdateDto 요소 null값 체크
    String title = itemUpdateDto.getTitle() != null ? itemUpdateDto.getTitle() : item.getTitle();
    String description = itemUpdateDto.getDescription() != null ? itemUpdateDto.getDescription() : item.getDescription();
    List<String> imageUrl = itemUpdateDto.getImages() != null ? itemUpdateDto.getImages() : item.getImages();
    Item.ItemStatus itemStatus = itemUpdateDto.getItemStatus() != null ? itemUpdateDto.getItemStatus() : item.getItemStatus();

    Item.builder()
        .itemId(itemId)
        .title(title)
        .description(description)
        .images(imageUrl)
        .itemStatus(itemStatus)
        .user(user) // User 객체 대신 userId 사용
        .build();


    return ItemUpdateResponseDto.builder()
        .title(title)
        .description(description)
        .images(imageUrl)
        .itemStatus(itemStatus)
        .build();
  }


  //  물건 삭제
  public void deleteItem(Integer itemId, Integer userId) {
    Item item = findItemByItemId(itemId);
    // 사용자 ID 일치 여부 확인
    if (!item.getUser().getUserId().equals(userId)) {
      // 여기서 사용자 ID가 일치하지 않으면 오류를 발생
      throw new BusinessException(NOT_ITEM_OWNER);
    }
    itemsRepository.delete(item);
  }

  //  물건 검색
  //  ex - /items/search?name=메롱!
//  public Item searchItems() {
//    return null;
//  }
  // 물건 검색

}


