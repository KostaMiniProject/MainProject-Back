package kosta.main.items.service;

import jakarta.validation.Valid;
import kosta.main.categories.entity.Category;
import kosta.main.categories.repository.CategoriesRepository;
import kosta.main.global.dto.PageInfo;
import kosta.main.global.dto.PageResponseDto;
import kosta.main.global.s3upload.service.ImageService;
import kosta.main.items.dto.ItemPageDTO;
import kosta.main.global.error.exception.BusinessException;
import kosta.main.global.error.exception.CommonErrorCode;
import kosta.main.items.dto.ItemDetailResponseDTO;
import kosta.main.items.dto.ItemUpdateDTO;
import kosta.main.items.dto.ItemUpdateResponseDTO;
import kosta.main.items.entity.Item;
import kosta.main.items.dto.ItemSaveDTO;
import kosta.main.items.repository.ItemsRepository;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kosta.main.global.error.exception.CommonErrorCode.*;

@Service
@Transactional
@AllArgsConstructor
public class ItemsService {

  private final UsersRepository usersRepository;
  private final ItemsRepository itemsRepository;
  private final ImageService imageService;
  private final CategoriesRepository categoriesRepository;

  /**
   * 물건 생성
   *
   * @param user
   * @param itemSaveDTO 물건 저장 DTO
   * @param files
   */
  public void addItem(User user,
                      ItemSaveDTO itemSaveDTO,
                      List<MultipartFile> files) {

//    # sudo 코드
//    1. Controller에서 ItemSaveDTO값을 받아온다.
//    2. 추가할 내용을 담는 용도로 Item 객체(newItem)를 생성한다.
//    3. ItemSaveDTO 내에서 getter을 이용해 추가하고 싶은 값을 꺼낸다.
//    4. 꺼낸값을 newItem 객체의 setter를 통해 값을 넣어준다.
//    5. 3번 4번 과정을 반복한다.
//      (user, bid, category, title, description, imageUrl)
//    6. itemsRepository의 save 메서드를 이용해서 newItem을 DB에 적용시켜준다.

//    # SETTER 사용
//    Item newItem1 = new Item();
//    newItem1.setTitle(itemSaveDTO.getTitle());
//    newItem1.setDescription(itemSaveDTO.getDescription());
//    newItem1.setImageUrl(itemSaveDTO.getImageUrl());

    // 사용자와 카테고리 정보 조회
//    Category category = categoriesRepository.findById(itemSaveDTO.getCategoryId())
//            .orElseThrow(() -> new RuntimeException("Category not found"));
    //유저 미발견 에러 추가
    if(user == null) throw new BusinessException(USER_NOT_FOUND);

    List<String> imagePaths = files.stream().map(imageService::resizeToBasicSizeAndUpload).toList();
    String categoryName = itemSaveDTO.getCategory();
    Category category = categoriesRepository.findByCategoryName(categoryName)
            .orElseThrow(() -> new BusinessException(CATEGORY_NOT_FOUND));
    // Item 객체 생성
    Item newItem = Item.builder()
        .user(user)
        .category(category)
        .title(itemSaveDTO.getTitle())
        .description(itemSaveDTO.getDescription())
        .images(imagePaths)
        .build();

    // Item 저장
    itemsRepository.save(newItem);
  }

  /**
   * 물건 목록 조회
   *
   * @param pageable
   * @return
   */
  @Transactional(readOnly = true)
  public PageResponseDto<List<ItemPageDTO>> getItems(User user, Pageable pageable) {
    Optional<User> userByEmail = usersRepository.findUserByEmail(user.getEmail());
    if(userByEmail.isEmpty()) throw new BusinessException(USER_NOT_FOUND);
    else user = userByEmail.get();
    List<Item> items = user.getItems();
    int size = items.size();
    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), size);
    List<ItemPageDTO> items1 = items.subList(start, end).stream().map(ItemPageDTO::from).toList();
    Page<Item> byUserUserId = new PageImpl<>(items.subList(start, end), pageable, size);
    return new PageResponseDto<>(items1, PageInfo.of(byUserUserId));
  }
  public Page<ItemPageDTO> getCanBidItems(Integer userId, Pageable pageable) {
    Page<Item> byUserUserId = itemsRepository.findByUser_UserIdAndIsBiding(userId, Item.IsBiding.NOT_BIDING ,pageable);
    return byUserUserId.map(ItemPageDTO::from);
  }
  public Page<ItemPageDTO> getItems(Integer userId, Pageable pageable) {
    Page<Item> byUserUserId = itemsRepository.findByUser_UserId(userId ,pageable);
    return byUserUserId.map(ItemPageDTO::from);
  }


  /**
   * 물건 상세 조회
   *
   * @param itemId
   * @return
   */
  public ItemDetailResponseDTO getFindById(int itemId) {
    Item itemByItemId = findItemByItemId(itemId);
    return ItemDetailResponseDTO.of(itemByItemId);
  }

  private Item findItemByItemId(int itemId) {
    return itemsRepository.findById(itemId).orElseThrow(() -> new BusinessException(CommonErrorCode.USER_NOT_FOUND));
  }


  /**
   * 물건 수정
   *
   * @param itemId
   * @param itemUpdateDTO 물건 수정 DTO
   * @param files
   * @param user
   * @return
   */
  public ItemUpdateResponseDTO updateItem(Integer itemId,
                                          @Valid ItemUpdateDTO itemUpdateDTO,
                                          List<MultipartFile> files, User user) {
//    # sudo 코드
//    1. Controller에서 itemId와 ItemUpdateDTO값을 받아온다.
//    2. 수정할 내용을 담는 용도인 Item 객체(updateItem)를 생성한다.
//    3. itemRepository의 getFindById 메서드와 itemId를 이용해 updateItem 객체를 초기화한다.
//    4. itemUpdateDTO에 포함된 요소를 한개씩 getter를 통해서 꺼내 null인지 확인한다.
//    5. 만약 null이 아니라면 updateItem 객체의 setter를 통해 값을 넣어준다.
//    6. 4번 5번 과정을 반복한다.
//      (title, description, imageUrl, itemStatus)
//    7. itemsRepository의 save 메서드를 이용해서 updateItem을 DB에 적용시켜준다.

//    # SETTER 사용
//    Item updateItem1 = getFindById(itemId);
//
//    if (itemUpdateDTO.getTitle() != null) {
//      updateItem1.setTitle(itemUpdateDTO.getTitle());
//    }
//    if (itemUpdateDTO.getDescription() != null) {
//      updateItem1.setDescription(itemUpdateDTO.getDescription());
//    }
//    if (itemUpdateDTO.getImageUrl() != null) {
//      updateItem1.setImageUrl(itemUpdateDTO.getImageUrl());
//    }
//    if (itemUpdateDTO.getItemStatus() != null) {
//      updateItem1.setItemStatus(itemUpdateDTO.getItemStatus());
//    }
    List<String> imagePath = new ArrayList<>(files.stream().map(imageService::resizeToBasicSizeAndUpload).toList());
    itemUpdateDTO.updateImagePath(imagePath);
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


//    itemUpdateDTO 요소 null값 체크
    String title = itemUpdateDTO.getTitle() != null ? itemUpdateDTO.getTitle() : item.getTitle();
    String description = itemUpdateDTO.getDescription() != null ? itemUpdateDTO.getDescription() : item.getDescription();
    List<String> imageUrl = itemUpdateDTO.getImages() != null ? itemUpdateDTO.getImages() : item.getImages();
    Item.ItemStatus itemStatus = itemUpdateDTO.getItemStatus() != null ? itemUpdateDTO.getItemStatus() : item.getItemStatus();

    Item itemUpdate = Item.builder()
        .itemId(itemId)
        .title(title)
        .description(description)
        .images(imageUrl)
        .itemStatus(itemStatus)
        .user(user) // User 객체 대신 userId 사용
        .build();

    itemsRepository.save(itemUpdate);


    return ItemUpdateResponseDTO.builder()
        .title(title)
        .description(description)
        .images(imageUrl)
        .itemStatus(itemStatus)
        .build();
  }


  /**
   * 물건 삭제
   *
   * @param itemId
   * @param userId
   */
  public void deleteItem(Integer itemId, Integer userId) {
    Item item = findItemByItemId(itemId);
    
    // 사용자 ID 일치 여부 확인
    if (!item.getUser().getUserId().equals(userId)) {
      // 여기서 사용자 ID가 일치하지 않으면 오류를 발생
      throw new BusinessException(NOT_ITEM_OWNER);
    }
    // 입찰중인 경우 삭제 불가 + 교환 게시글에 사용중이어도 삭제 불가
    if (item.getIsBiding() == Item.IsBiding.BIDING) {
      throw new BusinessException(ALREADY_BIDDING_ITEM);
    }

    itemsRepository.delete(item);
  }

  /**
   * 물건 검색
   * ex - /items/search?keyword=제목1
   *
   * @param keyword
   * @return
   */
  @Transactional(readOnly = true)
  public Page<ItemPageDTO> searchItems(String keyword,User user,Pageable pageable) {
    if(user == null) {
      throw new BusinessException(USER_NOT_FOUND);
    }
    Page<Item> searchItemsByUser
              = itemsRepository.findByTitleOrDescriptionOrCategoryNameContainingAndItemStatusAndIsBidingAndUserId(keyword, user.getUserId(), pageable);
      List<ItemPageDTO> list = searchItemsByUser.map(ItemPageDTO::from).stream().toList();
      return new PageImpl<ItemPageDTO>(list, searchItemsByUser.getPageable(), searchItemsByUser.getTotalElements());
    }
}


