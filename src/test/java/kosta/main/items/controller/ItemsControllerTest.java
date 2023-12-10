package kosta.main.items.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kosta.main.ControllerTest;
import kosta.main.global.annotation.WithMockCustomUser;
import kosta.main.items.ItemStubData;
import kosta.main.items.dto.ItemDetailResponseDTO;
import kosta.main.items.dto.ItemPageDTO;
import kosta.main.items.dto.ItemSaveDTO;
import kosta.main.items.dto.ItemUpdateDTO;
import kosta.main.items.entity.Item;
import kosta.main.items.service.ItemsService;
import kosta.main.users.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.generate.RestDocumentationGenerator;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@WebMvcTest(ItemsController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ItemsControllerTest extends ControllerTest {

    public static final int ITEM_ID = 1;
    public static final int ONE_ACTION = 1;
    @Autowired
    private RestDocumentationResultHandler restDocs;
    @Autowired
    private ObjectMapper objectMapper;

    private ItemStubData itemStubData;

    @MockBean
    ItemsService itemsService;

    @BeforeEach
    public void setup() {
        itemStubData = new ItemStubData();
    }
    private final String BASE_URL = "/api/items";
    @Test
    @WithMockCustomUser
    @DisplayName("물건 추가 성공 테스트")
    void addItem() throws Exception {

        //Given
        Item bidItem = itemStubData.getBidItem();
        ItemSaveDTO itemSaveDto = itemStubData.getItemSaveDTO();
        String content = objectMapper.writeValueAsString(itemSaveDto);
        MockMultipartFile file = itemStubData.getMockMultipartFile();

        MockPart itemSaveDto1 = new MockPart("itemSaveDTO", content.getBytes(StandardCharsets.UTF_8));
        itemSaveDto1.getHeaders().setContentType(APPLICATION_JSON);

        //When
        doNothing().when(itemsService).addItem(Mockito.any(User.class),Mockito.any(ItemSaveDTO.class), Mockito.anyList());

        //Then
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.multipart(HttpMethod.POST,BASE_URL)
                        .file(file)
                        .part(itemSaveDto1)
                        .header("Authorization", "Bearer yourAccessToken")
                        );


        perform
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        requestParts(
                                partWithName("itemSaveDTO").description("아이템 저장 정보"),
                                partWithName("file").description("이미지 파일")
                        ),
                        requestPartFields("itemSaveDTO",
                                fieldWithPath("title").type(JsonFieldType.STRING).description("물건 제목"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("물건 내용"),
                                fieldWithPath("imageUrl").type(JsonFieldType.ARRAY).description("물건의 이미지를 담는 배열(내부 로직용입니다 안적어도됨)").optional()
                        )
                ));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("물건 목록 조회 성공 테스트")
    void getItems() throws Exception {

        // given
        Page<ItemPageDTO> itemPageDTOs = itemStubData.getItemPageDTOs();
        // when
        given(itemsService.getItems(Mockito.anyInt(), Mockito.any(Pageable.class))).willReturn(itemPageDTOs);
        ResultActions perform = mockMvc.perform(get(BASE_URL)
                .header("Authorization", "Bearer yourAccessToken")
        );
        // then

        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("물건 목록 정보를 감싸고 있는 배열"),
                                fieldWithPath("data.[].itemId").type(JsonFieldType.NUMBER).description("물건 ID"),
                                fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("물건 제목"),
                                fieldWithPath("data.[].description").type(JsonFieldType.STRING).description("물건에 대한 설명"),
                                fieldWithPath("data.[].itemStatus").type(JsonFieldType.STRING).description("물건의 상태(PUBLIC, PRIVATE, DELETED)"),
                                fieldWithPath("data.[].images").type(JsonFieldType.STRING).description("물건의 가장 첫번째 이미지"),
                                fieldWithPath("data.[].crateAt").type(JsonFieldType.NULL).description("물건 생성 시각"),
                                fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보를 감싸고 있는 배열"),
                                fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("현재 페이지 숫자"),
                                fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 크기(한 번에 몇개의 정보를 가져올지"),
                                fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("전체 데이터 개수"),
                                fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 숫자")
                        )
                ));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("물건 조회 성공 테스트")
    void getFindById() throws Exception {
        // given
        ItemDetailResponseDTO itemDetailResponse = itemStubData.getItemDetailResponse();

        given(itemsService.getFindById(Mockito.anyInt())).willReturn(itemDetailResponse);
        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + "/{itemId}", ITEM_ID));

        // then


        result
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("itemId").description("물건 ID")
                        ),
                        responseFields(
                                fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("물건 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("물건 제목"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("물건에 대한 설명"),
                                fieldWithPath("itemStatus").type(JsonFieldType.STRING).description("물건의 상태(PUBLIC, PRIVATE, DELETED)"),
                                fieldWithPath("images").type(JsonFieldType.ARRAY).description("물건의 가장 첫번째 이미지"),
                                fieldWithPath("isBiding").type(JsonFieldType.STRING).description("물건이 입찰관련 상태를 나타내는 스테이터스(NOT_BIDING, BIDING) "),
                                fieldWithPath("createAt").type(JsonFieldType.NULL).description("물건 생성 시각"),
                                fieldWithPath("user").type(JsonFieldType.OBJECT).description("유저 정보를 담고있는 객체"),
                                fieldWithPath("user.userId").type(JsonFieldType.NUMBER).description("유저 ID"),
                                fieldWithPath("user.email").type(JsonFieldType.STRING).description("유저 이메일"),
                                fieldWithPath("user.name").type(JsonFieldType.STRING).description("유저 이름"),
                                fieldWithPath("user.address").type(JsonFieldType.STRING).description("유저 주소"),
                                fieldWithPath("user.phone").type(JsonFieldType.STRING).description("유저 전화번호"),
                                fieldWithPath("user.profileImage").type(JsonFieldType.STRING).description("유저 프로필 이미지")
                        )
                ));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("물건 수정 성공 테스트")
    void updateItem() throws Exception {
        //Given
        ItemUpdateDTO itemUpdateDto = itemStubData.getItemUpdateDto();

        String content = objectMapper.writeValueAsString(itemUpdateDto);
        MockMultipartFile file = itemStubData.getMockMultipartFile();

        MockPart itemUpdateDto1 = new MockPart("itemUpdateDTO", content.getBytes(StandardCharsets.UTF_8));
        itemUpdateDto1.getHeaders().setContentType(APPLICATION_JSON);
        given(itemsService
                .updateItem(Mockito.anyInt(),Mockito.any(ItemUpdateDTO.class), Mockito.anyList(),Mockito.any(User.class)))
                .willReturn(itemStubData.getItemUpdateResponseDto());
        //When

        //Then
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.multipart(HttpMethod.PUT,BASE_URL+"/{itemId}", ITEM_ID)
                        .file(file)
                        .part(itemUpdateDto1)
                        .header("Authorization", "Bearer yourAccessToken")
                        .requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, BASE_URL+"/{itemId}"));


        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("itemId").description("물건 ID")
                                ),
                        requestParts(
                                partWithName("itemUpdateDTO").description("유저 업데이트 정보"),
                                partWithName("file").description("유저 프로필 사진")
                        ),
                        requestPartFields("itemUpdateDTO",
                                fieldWithPath("title").type(JsonFieldType.STRING).description("물건 제목"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("물건 설명"),
                                fieldWithPath("itemStatus").type(JsonFieldType.STRING).description("물건 상태(PUBLIC, PRIVATE, DELETED)"),
                                fieldWithPath("images").type(JsonFieldType.ARRAY).description("이미지 저장경로(안넣어도 됩니다 내부 로직용임)").optional()
                        ),
                        responseFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("물건 제목"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("물건 설명"),
                                fieldWithPath("itemStatus").type(JsonFieldType.STRING).description("물건 상태(PUBLIC, PRIVATE, DELETED)"),
                                fieldWithPath("images").type(JsonFieldType.ARRAY).description("이미지 저장경로")

                        )

                ));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("물건 삭제 성공 테스트")
    void deleteItem() throws Exception {
        //given

        //when
        doNothing().when(itemsService).deleteItem(Mockito.anyInt() , Mockito.anyInt());
        ResultActions actions = mockMvc.perform(
                RestDocumentationRequestBuilders.delete(BASE_URL+"/{itemId}", ITEM_ID)
                        .header("Authorization", "Bearer yourAccessToken")
                        );
        verify(itemsService, times(ONE_ACTION)).deleteItem(Mockito.anyInt(),Mockito.anyInt());
        //then
        actions
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("itemId").description("물건 ID")
                        )
                ));
    }

////    @Test 덜구현된듯 ㅠ
//    @WithMockCustomUser
//    @DisplayName("물건 검색")
//    void searchItems(){
//        //given
//
//        itemStubData.get
//        given(itemsService.searchItems(Mockito.anyString())).willReturn()
//        //when
//
//        //then
//    }
}