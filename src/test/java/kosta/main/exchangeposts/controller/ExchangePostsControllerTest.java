package kosta.main.exchangeposts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kosta.main.communityposts.dto.CommunityPostDetailDto;
import kosta.main.communityposts.dto.CommunityPostListDto;
import kosta.main.exchangeposts.ExchangePostStubData;
import kosta.main.exchangeposts.dto.ExchangePostDTO;
import kosta.main.exchangeposts.dto.ExchangePostListDTO;
import kosta.main.exchangeposts.service.ExchangePostsService;
import kosta.main.global.annotation.WithMockCustomUser;
import kosta.main.items.entity.Item;
import kosta.main.users.UserStubData;
import kosta.main.users.controller.UsersController;
import kosta.main.users.dto.UserCreateDto;
import kosta.main.users.dto.UserCreateResponseDto;
import kosta.main.users.entity.User;
import kosta.main.users.service.UsersService;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
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

        MockMvcBuilders.webAppContextSetup(webApplicationContext)
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

        given(exchangePostsService.createExchangePost(Mockito.any(User.class),Mockito.any(ExchangePostDTO.class))).willReturn(EXCHANGE_POST_ID);
        //when

        ResultActions result = mockMvc.perform(
                post(BASIC_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf())
        );

        //then
        result.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("교환 게시글 목록 조회 성공 테스트")
    void getAllExchangePosts() throws Exception {
        // given
        List<ExchangePostListDTO> exchangePostListDTO = exchangePostStubData.getExchangePostListDTO();
        // when
        when(exchangePostsService.findAllExchangePosts()).thenReturn(exchangePostListDTO);

        // then
        this.mockMvc.perform(get(BASIC_URL))
                .andDo(print())
                .andExpect(status().isOk());
//                .andDo(restDocs.document());
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
                put(BASIC_URL+"/{exchangePostId}",EXCHANGE_POST_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf())
        );

        //then
        result.andDo(print())
                .andExpect(status().isOk());
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
                .andExpect(status().isOk());
    }
}