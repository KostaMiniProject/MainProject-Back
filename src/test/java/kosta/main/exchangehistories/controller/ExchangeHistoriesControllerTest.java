package kosta.main.exchangehistories.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kosta.main.ControllerTest;
import kosta.main.exchangehistories.ExchangeHistoriesStubData;
import kosta.main.exchangehistories.dto.ExchangeHistoriesResponseDTO;
import kosta.main.exchangehistories.service.ExchangeHistoriesService;
import kosta.main.global.annotation.WithMockCustomUser;
import kosta.main.users.UserStubData;
import kosta.main.users.controller.UsersController;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({SpringExtension.class})
@WebMvcTest(ExchangeHistoriesController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ExchangeHistoriesControllerTest extends ControllerTest {


    public static final String BASIC_URL = "/api/users/histories";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestDocumentationResultHandler restDocs;

    @MockBean
    private ExchangeHistoriesService exchangeHistoriesService;

    private ExchangeHistoriesStubData exchangeHistoriesStubData;
    @BeforeEach
    public void setup() {
        exchangeHistoriesStubData = new ExchangeHistoriesStubData();
    }

//    @Test
//    @WithMockCustomUser
//    @DisplayName("교환 내역 조회")
//    void getExchangeHistories() throws Exception {
//        //given
//        Page<ExchangeHistoriesResponseDTO> exchangeHistoriesResponseDTOs = exchangeHistoriesStubData.();
//
//        given(exchangeHistoriesService.getExchangeHistories(Mockito.any(User.class), Mockito.any(Pageable.class))).willReturn(exchangeHistoriesResponseDTOs);
//        //when
//
//        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get(BASIC_URL)
//                .param("page", "0")
//                .header("Authorization", "Bearer yourAccessToken"));
//
//        //then
//        result.andDo(print())
//                .andExpect(status().isOk())
//                .andDo(restDocs.document(
//                        requestHeaders(
//                                headerWithName("Authorization").description("액세스 토큰")
//                        ),
//                        queryParameters(
//                                parameterWithName("page").description("현재 페이지를 표시하는 파라미터")
//                        ),
//                        responseFields(
//                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("커뮤니티게시글을 감싸고 있는 배열"),
//                                fieldWithPath("data.[].createdAt").type(JsonFieldType.NULL).description("교환이 성립된 날짜"),
//                                fieldWithPath("data.[].posterName").type(JsonFieldType.STRING).description("나의 닉네임"),
//                                fieldWithPath("data.[].posterAddress").type(JsonFieldType.STRING).description("나의 주소"),
//                                fieldWithPath("data.[].posterProfileImage").type(JsonFieldType.STRING).description("나의 프로필 이미지"),
//                                fieldWithPath("data.[].posterItem").type(JsonFieldType.ARRAY).description("내가 건내준 물건의 리스트 (물건 이름, 물건 상세내용, 물건 대표이미지)"),
//                                fieldWithPath("data.[].posterItem.[].itemId").type(JsonFieldType.NUMBER).description("내 물건 ID"),
//                                fieldWithPath("data.[].posterItem.[].title").type(JsonFieldType.STRING).description("내 물건 제목"),
//                                fieldWithPath("data.[].posterItem.[].description").type(JsonFieldType.STRING).description("내 물건 상세내용"),
//                                fieldWithPath("data.[].posterItem.[].imageUrl").type(JsonFieldType.STRING).description("내 물건의 이미지"),
//                                fieldWithPath("data.[].bidderName").type(JsonFieldType.STRING).description("상대방 닉네임"),
//                                fieldWithPath("data.[].bidderAddress").type(JsonFieldType.STRING).description("상대방 사용자의 주소"),
//                                fieldWithPath("data.[].bidderProfileImage").type(JsonFieldType.STRING).description("상대방 프로필 이미지"),
//                                fieldWithPath("data.[].bidderItem").type(JsonFieldType.ARRAY).description("상대방이 건내준 물건의 리스트 (물건 이름, 물건 상세내용, 물건 대표이미지)"),
//                                fieldWithPath("data.[].bidderItem.[].title").type(JsonFieldType.STRING).description("상대방 물건 제목"),
//                                fieldWithPath("data.[].bidderItem.[].description").type(JsonFieldType.STRING).description("상대방 물건 상세내용"),
//                                fieldWithPath("data.[].bidderItem.[].imageUrl").type(JsonFieldType.ARRAY).description("상대방 물건 이미지"),
//                                fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보를 감싸고 있는 배열"),
//                                fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("현재 페이지 숫자"),
//                                fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 크기(한 번에 몇개의 정보를 가져올지"),
//                                fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("전체 데이터 개수"),
//                                fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 숫자")
//                        )
//                ));
//    }
}