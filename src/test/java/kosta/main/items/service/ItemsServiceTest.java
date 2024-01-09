package kosta.main.items.service;

import kosta.main.categories.CategoryStubData;
import kosta.main.categories.entity.Category;
import kosta.main.categories.repository.CategoriesRepository;
import kosta.main.communityposts.CommunityPostStubData;
import kosta.main.exchangeposts.ExchangePostStubData;
import kosta.main.global.s3upload.service.ImageService;
import kosta.main.items.ItemStubData;
import kosta.main.items.dto.ItemSaveDTO;
import kosta.main.items.dto.ItemUpdateDTO;
import kosta.main.items.dto.ItemUpdateResponseDTO;
import kosta.main.items.entity.Item;
import kosta.main.items.repository.ItemsRepository;
import kosta.main.users.UserStubData;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ItemsServiceTest {

    public static final int ITEM_ID = 1;
    @InjectMocks
    ItemsService itemsService;
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private ItemsRepository itemsRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private CategoriesRepository categoriesRepository;

    private CategoryStubData categoryStubData;
    private ItemStubData itemStubData;
    private UserStubData userStubData;

    @BeforeEach
    void init(){
        categoryStubData = new CategoryStubData();
        itemStubData = new ItemStubData();
        userStubData = new UserStubData();

    }
    @Test
    void addItem() throws IOException {
        //given
        Category category = categoryStubData.getCategory();
        Item bidItem = itemStubData.getBidItem();
        User user = userStubData.getUser();
        ItemSaveDTO itemSaveDTO = itemStubData.getItemSaveDTO();
        MockMultipartFile mockMultipartFile = itemStubData.getMockMultipartFile();
        List<MultipartFile> multipartFiles = new ArrayList<>();
        multipartFiles.add(mockMultipartFile);

        given(imageService.resizeToBasicSizeAndUpload(Mockito.any(MultipartFile.class))).willReturn("imageFile");
        given(categoriesRepository.findByCategoryName(Mockito.anyString())).willReturn(Optional.of(category));
        given(itemsRepository.save(Mockito.any(Item.class))).willReturn(bidItem);
        //when
        itemsService.addItem(user,itemSaveDTO,multipartFiles);
        //then
        //아무것도 반환을 하지 않기 때문에 일단 무엇을 체크해야할지 정확히 모르겠음
    }

    @Test
    void updateItem() throws IOException {
        User user = userStubData.getUser();
        Item noBidItem = itemStubData.getNoBidItem();
        ItemUpdateDTO itemUpdateDto = itemStubData.getItemUpdateDto();
        List<MultipartFile> files = new ArrayList<>();
        MockMultipartFile mockMultipartFile = itemStubData.getMockMultipartFile();
        files.add(mockMultipartFile);

        //given
        given(imageService.resizeToBasicSizeAndUpload(Mockito.any(MultipartFile.class))).willReturn("image");
        given(itemsRepository.findById(Mockito.anyInt())).willReturn(Optional.of(noBidItem));
        given(itemsRepository.save(Mockito.any(Item.class))).willReturn(noBidItem);

        //when
        ItemUpdateResponseDTO itemUpdateResponseDTO = itemsService.updateItem(ITEM_ID, itemUpdateDto, files, user);
        //then
        Assertions.assertThat(itemUpdateResponseDTO.getTitle()).isEqualTo(itemUpdateDto.getTitle());
        Assertions.assertThat(itemUpdateResponseDTO.getDescription()).isEqualTo(itemUpdateDto.getDescription());
        Assertions.assertThat(itemUpdateResponseDTO.getItemStatus()).isEqualTo(itemUpdateDto.getItemStatus());
        Assertions.assertThat(itemUpdateResponseDTO.getImages().size()).isEqualTo(itemUpdateDto.getImages().size());

    }

}