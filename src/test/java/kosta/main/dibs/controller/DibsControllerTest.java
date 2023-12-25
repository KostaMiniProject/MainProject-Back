package kosta.main.dibs.controller;

import kosta.main.ControllerTest;

import kosta.main.dibs.DibStubData;
import kosta.main.dibs.dto.DibbedExchangePostDTO;
import kosta.main.dibs.service.DibsService;
import kosta.main.global.annotation.WithMockCustomUser;
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
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@WebMvcTest(DibsController.class)
@MockBean(JpaMetamodelMappingContext.class)
class DibsControllerTest extends ControllerTest {


    public static final String BASIC_URL = "/api/exchange-posts";
    public static final int EXCHANGE_POST_ID = 1;
    public static final int USER_ID = 1;

    @Autowired
    private RestDocumentationResultHandler restDocs;

    @MockBean
    private DibsService dibsService;

    private UserStubData userStubData;
    private DibStubData dibStubData;

    @BeforeEach
    public void setup() {
        userStubData = new UserStubData();
        dibStubData = new DibStubData();
    }

    @Test
    @WithMockCustomUser
    @DisplayName("찜하기 성공 테스트")
    void toggleDib() throws Exception {
        //given
        User user = userStubData.getUser();
        //when
        doNothing().when(dibsService).toggleDib(Mockito.anyInt(),Mockito.anyInt());
        ResultActions result = mockMvc.perform(post(BASIC_URL + "/{exchangePostId}/dibs", EXCHANGE_POST_ID)
                .header("Authorization", "Bearer yourAccessToken")
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
                        )
                ));

    }

    @Test
    @WithMockCustomUser
    @DisplayName("유저 찜 보기")
    void getUserDibs() throws Exception {
        //given
        List<DibbedExchangePostDTO> dibbedExchangePostDTOs = dibStubData.getDibbedExchangePostDTOs();
        //when
        given(dibsService.getUserDibs(Mockito.any(User.class))).willReturn(dibbedExchangePostDTOs);
        ResultActions result = mockMvc.perform(get(BASIC_URL + "/dibs")
                .header("Authorization", "Bearer yourAccessToken")
                );
        //then

        result.andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("[].exchangePostId").type(JsonFieldType.NUMBER).description("교환 게시글 ID"),
                                fieldWithPath("[].title").type(JsonFieldType.STRING).description("교환 게시글 제목"),
                                fieldWithPath("[].representativeImageUrl").type(JsonFieldType.STRING).description("교환 게시글 물건 대표(첫번째) 이미지"),
                                fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("찜한 시각")
                        )
                ));
    }
}