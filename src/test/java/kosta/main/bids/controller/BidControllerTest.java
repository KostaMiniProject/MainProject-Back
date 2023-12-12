package kosta.main.bids.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kosta.main.ControllerTest;
import kosta.main.bids.BidStubData;
import kosta.main.bids.dto.BidsDTO;
import kosta.main.bids.service.BidService;
import kosta.main.exchangeposts.ExchangePostStubData;
import kosta.main.global.annotation.WithMockCustomUser;
import kosta.main.users.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Description;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({SpringExtension.class})
@WebMvcTest(BidController.class)
@MockBean(JpaMetamodelMappingContext.class)
class BidControllerTest extends ControllerTest {


    public static final int BID_ID = 1;
    public static final int EXCHANGE_POST_ID = 1;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestDocumentationResultHandler restDocs;

    @MockBean
    private BidService bidService;

    private final String BASE_URL = "/api/exchange-posts";

    private BidStubData bidStubData;

    @BeforeEach
    public void setup() {
        bidStubData = new BidStubData();
    }

    @Test
    @WithMockCustomUser
    @Description("입찰을 수행하는 기능")
    void createBid() throws Exception {

        //given
        BidsDTO bidsDTO = bidStubData.getBidsDTO();
        given(bidService.createBid(Mockito.any(User.class), Mockito.anyInt(), Mockito.any(BidsDTO.class))).willReturn(BID_ID);
        String content = objectMapper.writeValueAsString(bidsDTO);

        //when


        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL + "/{exchangePostId}/bids", EXCHANGE_POST_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .header("Authorization", "Bearer yourAccessToken"));

        //then
        result.andDo(print())
                .andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("exchangePostId").description("게시글 ID")
                        ),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("임시로 넣어놓은 데이터"),
                                fieldWithPath("itemIds").type(JsonFieldType.ARRAY).description("입찰에 사용되는 아이템의 ID 목록")
                        )
                ));
    }

    @Test
    void getAllBidsForPost() {
    }

    @Test
    void getBidById() {
    }

    @Test
    void updateBid() {
    }

    @Test
    void deleteBid() {
    }

    @Test
    void completeExchange() {
    }

    @Test
    void reserveExchange() {
    }

    @Test
    void denyBid() {
    }

    @Test
    void undoDenyBid() {
    }

    @Test
    void getDeniedBidsForPost() {
    }
}