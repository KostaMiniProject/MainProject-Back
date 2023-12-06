package kosta.main.exchangeposts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kosta.main.exchangeposts.ExchangePostStubData;
import kosta.main.exchangeposts.dto.ExchangePostDTO;
import kosta.main.exchangeposts.dto.ExchangePostListDTO;
import kosta.main.exchangeposts.service.ExchangePostsService;
import kosta.main.global.annotation.WithMockCustomUser;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(ExchangePostsController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(kosta.main.RestDocsConfiguration.class)
class ExchangePostsControllerTest {

    public static final int EXCHANGE_POST_ID = 1;
    public static final String BASIC_URL = "/api/exchange-posts";
    public static final int ONE_ACTION = 1;
    public static final String DESC_EXCHANGE_POST_ID = "교환 게시글 ID";
    public static final String DESC_EXCHANGE_POST_TITLE = "물물교환 게시글 제목";
    public static final String DESC_PREFER_ITEM = "물물교환 선호물품";
    public static final String DESC_ADDRESS = "거래 희망 주소";
    public static final String DESC_CONTENT = "물물교환 게시글 내용";
    public static final String DESC_EXCHANGE_POST_STATUS = "해당 게시글의 상태";
    public static final String DESC_CREATED_AT = "물물교환 게시글 작성일(현재 널로 처리되어있는데 수정하겠습니다)";
    public static final String DESC_IMAGE_URL = "교환 게시글에 등록된 item의 대표이미지 URL";
    public static final String DESC_BID_COUNT = "해당 교환 게시글에 등록된 입찰의 갯수를 세서 Integer 값으로 반환";
    public static final String DATA = "전달하는 데이터 배열";
    public static final String DESC_PAGEINFO = "페이지 정보를 감싸고 있는 배열";
    public static final String DESC_PAGESIZE = "현재 페이지 숫자";
    public static final String DESC_SIZE = "페이지 크기(한 번에 몇개의 정보를 가져올지";
    public static final String DESC_TOTAL_ELEMENTS = "전체 데이터 개수";
    public static final String DESC_TOTALPAGES = "전체 페이지 숫자";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestDocumentationResultHandler restDocs;

    @MockBean
    private ExchangePostsService exchangePostsService;
    private ExchangePostStubData exchangePostStubData;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {

        exchangePostStubData = new ExchangePostStubData();

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .alwaysDo(print())														// 이건 왜하는지 모르겠음.
                .alwaysDo(restDocs)														// 재정의한 핸들러를 적용함. 적용하면 일반 document에도 적용됨. 일반 document로 선언되면 그부분도 같이 생성됨에 유의해야 함.
                .addFilters(new CharacterEncodingFilter("UTF-8", true))					// 한글깨짐 방지 처리
                .build();
    }
    @Test
    @WithMockCustomUser
    @DisplayName("교환 게시글 생성")
    void createExchangePost() throws Exception {

        //given
        ExchangePostDTO exchangePostDTO = exchangePostStubData.getExchangePostDTO();
        String content = objectMapper.writeValueAsString(exchangePostDTO);

        given(exchangePostsService.createExchangePost(
                Mockito.any(User.class),
                Mockito.any(ExchangePostDTO.class)))
                .willReturn(exchangePostStubData.getResponseDto());
        //when

        ResultActions result = mockMvc.perform(
                post(BASIC_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", "Bearer yourAccessToken")
                        .with(csrf())
        );

        //then
        result.andDo(print())
                .andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("물물교환 게시글 제목"),
                                fieldWithPath("preferItems").type(JsonFieldType.STRING).description("물물교환 선호물품"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("거래 요청 주소지"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("물물교환 게시글 내용"),
                                fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("물건의 ID"),
                                fieldWithPath("exchangePostStatus").type(JsonFieldType.STRING).description("해당 게시글의 상태").optional()
                        ),
                        responseFields(
                                fieldWithPath("exchangePostId").type(JsonFieldType.NUMBER).description("물물교환 게시글 ID")
                        )
                ));

    }

    @Test
    @WithMockCustomUser
    @DisplayName("교환 게시글 목록 조회 성공 테스트")
    void getAllExchangePosts() throws Exception {
        // given
        Page<ExchangePostListDTO> exchangePostListDTOPage = exchangePostStubData.getExchangePostListDTOPage();
        // when
        when(exchangePostsService.findAllExchangePosts(Mockito.any(Pageable.class))).thenReturn(exchangePostListDTOPage);

        // then
        this.mockMvc.perform(get(BASIC_URL)
                        .header("Authorization", "Bearer yourAccessToken"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰").optional()
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description(DATA),
                                fieldWithPath("data.[].exchangePostId").type(JsonFieldType.NUMBER).description(DESC_EXCHANGE_POST_ID),
                                fieldWithPath("data.[].title").type(JsonFieldType.STRING).description(DESC_EXCHANGE_POST_TITLE),
                                fieldWithPath("data.[].preferItem").type(JsonFieldType.STRING).description(DESC_PREFER_ITEM),
                                fieldWithPath("data.[].address").type(JsonFieldType.STRING).description(DESC_ADDRESS),
                                fieldWithPath("data.[].exchangePostStatus").type(JsonFieldType.STRING).description(DESC_EXCHANGE_POST_STATUS),
                                fieldWithPath("data.[].createdAt").type(JsonFieldType.NULL).description(DESC_CREATED_AT),
                                fieldWithPath("data.[].imgUrl").type(JsonFieldType.STRING).description(DESC_IMAGE_URL),
                                fieldWithPath("data.[].bidCount").type(JsonFieldType.NUMBER).description(DESC_BID_COUNT),
                                fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description(DESC_PAGEINFO),
                                fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description(DESC_PAGESIZE),
                                fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description(DESC_SIZE),
                                fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description(DESC_TOTAL_ELEMENTS),
                                fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description(DESC_TOTALPAGES)
                        )
                ));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("교환 게시글 상세 조회 성공 테스트")
    void getExchangePostById() {
        //TODO 구현필요
    }

    @Test
    @WithMockCustomUser
    @DisplayName("교환 게시글 수정 성공 테스트")
    void updateExchangePost() throws Exception {
        //given
        ExchangePostDTO exchangePostDTO = exchangePostStubData.getExchangePostDTO();
        String content = objectMapper.writeValueAsString(exchangePostDTO);

        given(exchangePostsService.updateExchangePost(Mockito.any(User.class),Mockito.anyInt(),Mockito.any(ExchangePostDTO.class))).willReturn(EXCHANGE_POST_ID);
        //when

        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.put(BASIC_URL+"/{exchangePostId}",EXCHANGE_POST_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", "Bearer yourAccessToken")
                        .with(csrf())
        );

        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("exchangePostId").description("교환 게시글 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("물물교환 게시글 제목"),
                                fieldWithPath("preferItems").type(JsonFieldType.STRING).description("물물교환 선호물품"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("거래 요청 주소지"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("물물교환 게시글 내용"),
                                fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("물건의 ID"),
                                fieldWithPath("exchangePostStatus").type(JsonFieldType.STRING).description("해당 게시글의 상태").optional()
                        )
                ));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("교환 게시글 삭제 성공 테스트")
    void deleteExchangePost() throws Exception {

        //given
        doNothing().when(exchangePostsService).deleteExchangePost(Mockito.anyInt(),Mockito.any(User.class));
        //when
        ResultActions result = mockMvc.perform(
                delete(BASIC_URL+"/{exchangePostId}",EXCHANGE_POST_ID)
                        .with(csrf())
        );

        //then
        verify(exchangePostsService,times(ONE_ACTION)).deleteExchangePost(Mockito.anyInt(),Mockito.any(User.class));

        result.andDo(print())
                .andExpect(status().isNoContent());
    }
}