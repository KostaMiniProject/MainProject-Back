package kosta.main.items.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kosta.main.communityposts.CommunityPostStubData;
import kosta.main.communityposts.controller.CommunityPostsController;
import kosta.main.communityposts.dto.*;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.global.annotation.WithMockCustomUser;
import kosta.main.items.ItemStubData;
import kosta.main.items.dto.ItemSaveDto;
import kosta.main.items.dto.ItemUpdateDto;
import kosta.main.items.entity.Item;
import kosta.main.items.service.ItemsService;
import kosta.main.users.UserStubData;
import kosta.main.users.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(ItemsController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(kosta.main.RestDocsConfiguration.class)
class ItemsControllerTest {

    public static final int ITEM_ID = 1;
    public static final int ONE_ACTION = 1;
    @Autowired
    private RestDocumentationResultHandler restDocs;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private ItemStubData itemStubData;

    @MockBean
    ItemsService itemsService;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
        itemStubData = new ItemStubData();
        MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .alwaysDo(print())
                .alwaysDo(restDocs)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }
    private final String BASE_URL = "/api/items";
    @Test
    @WithMockCustomUser
    @DisplayName("물건 추가 성공 테스트")
    void addItem() throws Exception {

        //Given
        Item bidItem = itemStubData.getBidItem();
        ItemSaveDto itemSaveDto = itemStubData.getItemSaveDto();
        String content = objectMapper.writeValueAsString(itemSaveDto);
        MockMultipartFile file = itemStubData.getMockMultipartFile();

        MockPart itemSaveDto1 = new MockPart("itemSaveDto", content.getBytes(StandardCharsets.UTF_8));
        itemSaveDto1.getHeaders().setContentType(APPLICATION_JSON);

        //When
        doNothing().when(itemsService).addItem(Mockito.any(User.class),Mockito.any(ItemSaveDto.class), Mockito.anyList());

        //Then
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.multipart(HttpMethod.POST,BASE_URL)
                        .file(file)
                        .part(itemSaveDto1)
                        .with(csrf()));


        perform
                .andDo(print())
                .andExpect(status().isOk());
//                .andDo(restDocs.document());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("물건 목록 조회 성공 테스트")
    void getItems() throws Exception {

        // given
        List<Item> items = itemStubData.getItems();
        // when
        when(itemsService.getItems()).thenReturn(items);

        // then
        this.mockMvc.perform(get(BASE_URL))
                .andDo(print())
                .andExpect(status().isOk());
//                .andDo(restDocs.document());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("물건 조회 성공 테스트")
    void getFindById() throws Exception {
        // given
        Item bidItem = itemStubData.getBidItem();
        // when
        when(itemsService.getFindById(Mockito.anyInt())).thenReturn(bidItem);

        // then
        this.mockMvc.perform(get(BASE_URL+"/{itemId}",ITEM_ID))
                .andDo(print())
                .andExpect(status().isOk());
//                .andDo(restDocs.document())
    }

    @Test
    @WithMockCustomUser
    @DisplayName("물건 수정 성공 테스트")
    void updateItem() throws Exception {
        //Given
        Item bidItem = itemStubData.getBidItem();
        String content = objectMapper.writeValueAsString(bidItem);
        MockMultipartFile file = itemStubData.getMockMultipartFile();

        MockPart itemUpdateDto1 = new MockPart("itemUpdateDto", content.getBytes(StandardCharsets.UTF_8));
        itemUpdateDto1.getHeaders().setContentType(APPLICATION_JSON);
        given(itemsService.updateItem(Mockito.anyInt(),Mockito.any(ItemUpdateDto.class), Mockito.anyList())).willReturn(bidItem);
        //When

        //Then
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.multipart(HttpMethod.PUT,BASE_URL+"/{itemId}", ITEM_ID)
                        .file(file)
                        .part(itemUpdateDto1)
                        .with(csrf()));


        perform
                .andDo(print())
                .andExpect(status().isOk());
//                .andDo(restDocs.document());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("물건 삭제 성공 테스트")
    void deleteItem() throws Exception {
        //given

        //when
        doNothing().when(itemsService).deleteItem(Mockito.anyInt());
        ResultActions actions = mockMvc.perform(
                delete(BASE_URL+"/{itemId}", ITEM_ID)
                        .with(csrf()));
        verify(itemsService, times(ONE_ACTION)).deleteItem(Mockito.anyInt());
        //then
        actions
                .andDo(print())
                .andExpect(status().isOk());
    }
}