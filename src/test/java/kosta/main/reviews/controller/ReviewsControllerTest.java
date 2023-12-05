package kosta.main.reviews.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kosta.main.ControllerTest;
import kosta.main.reviews.ReviewStubData;
import kosta.main.reviews.dto.ReviewSaveDTO;
import kosta.main.reviews.entity.Review;
import kosta.main.reviews.service.ReviewsService;
import kosta.main.users.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@WebMvcTest(ReviewsController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ReviewsControllerTest extends ControllerTest {


    public static final int ONE_ACTION = 1;
    public static final String BASIC_URL = "/api/reviews";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestDocumentationResultHandler restDocs;

    @MockBean
    private ReviewsService reviewsService;

    private ReviewStubData reviewStubData;

    @BeforeEach
    public void setup(){
        ReviewStubData reviewStubData = new ReviewStubData();
    }
//    @Test
//    void addReviews() throws Exception {
//        ReviewSaveDTO reviewSaveDTO = reviewStubData.getReviewSaveDTO();
//        Review review = reviewStubData.getReview();
//
//        //given
//        verify(reviewsService,times(ONE_ACTION)).addReviews(Mockito.any(ReviewSaveDTO.class),Mockito.any(User.class));
//
//        //when
//        ResultActions action = mockMvc.perform(
//                get(BASIC_URL, 1)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer yourAccessToken")
//        );
//
//        //then
//
//        action.andDo(print())
//                .andExpect(status().isCreated())
//                .andDo(restDocs.document(
//                        requestHeaders(
//                                headerWithName("Authorization").description("액세스 토큰")
//                        ),
//                        requestFields(
//                                fieldWithPath("reviewedUserId").type(JsonFieldType.NUMBER).description("유저의 아이디로 사용되는 이메일"),
//                                fieldWithPath("rating").type(JsonFieldType.NUMBER).description("유저의 비밀번호"),
//                                fieldWithPath("review").type(JsonFieldType.STRING).description("유저의 이름")
//                        )
//                ));
//
//
//    }
}