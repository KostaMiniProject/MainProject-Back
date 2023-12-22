package kosta.main.bids.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kosta.main.ControllerTest;
import kosta.main.bids.BidStubData;
import kosta.main.bids.dto.*;
import kosta.main.bids.service.BidService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({SpringExtension.class})
@WebMvcTest(BidController.class)
@MockBean(JpaMetamodelMappingContext.class)
class BidControllerTest extends ControllerTest {


    public static final int BID_ID = 1;
    public static final int EXCHANGE_POST_ID = 1;
    public static final int ONE_ACTION = 1;

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
    @WithMockCustomUser
    @Description("한 교환게시글에 있는 입찰 목록을 모두 불러오는 기능")
    void getAllBidsForPost() throws Exception {
        //given
        BidListResponseDTO bidListResponseDTO = bidStubData.getBidListResponseDTO();
        List<BidListResponseDTO> bidListResponseDTOS = new ArrayList<>();
        bidListResponseDTOS.add(bidListResponseDTO);
        given(bidService.findAllBidsForPost(Mockito.anyInt(), Mockito.any(User.class))).willReturn(bidListResponseDTOS);

        //when

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + "/{exchangePostId}/bids", EXCHANGE_POST_ID)
                .header("Authorization", "Bearer yourAccessToken"));

        //then

        result.andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("exchangePostId").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].isOwner").type(JsonFieldType.BOOLEAN).description("교환 게시글 작성자인지를 체크"),
                                fieldWithPath("[].items").type(JsonFieldType.ARRAY).description("입찰에 사용되는 아이템의 상세 정보"),
                                fieldWithPath("[].items.[].title").type(JsonFieldType.STRING).description("아이템 제목"),
                                fieldWithPath("[].items.[].description").type(JsonFieldType.STRING).description("아이템 설명"),
                                fieldWithPath("[].items.[].imgUrl").type(JsonFieldType.STRING).description("아이템의 첫 번째 이미지 URL"),
                                fieldWithPath("[].items.[].createdAt").type(JsonFieldType.STRING).description("아이템 생성 시간")
                        )
                ));

    }

    @Test
    @WithMockCustomUser
    @Description("한 입찰에 대한 상세 정보를 제공하는 기능")
    void getBidById() throws Exception {
        //given
        BidDetailResponseDTO bidDetailResponseDTO = bidStubData.getBidDetailResponseDTO();
        given(bidService.findBidById(Mockito.anyInt(), Mockito.any(User.class))).willReturn(bidDetailResponseDTO);

        //when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + "/bids/{bidId}",BID_ID)
                .header("Authorization", "Bearer yourAccessToken"));

        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("bidId").description("입찰 ID")
                        ),
                        responseFields(
                                fieldWithPath("isOwner").type(JsonFieldType.BOOLEAN).description("교환 게시글 작성자인지를 체크"),
                                fieldWithPath("profile").type(JsonFieldType.OBJECT).description("유저 프로필 정보를 담고있는 객체"),
                                fieldWithPath("profile.userId").type(JsonFieldType.NUMBER).description("유저 ID"),
                                fieldWithPath("profile.name").type(JsonFieldType.STRING).description("유저 이름"),
                                fieldWithPath("profile.address").type(JsonFieldType.STRING).description("유저 주소"),
                                fieldWithPath("profile.imageUrl").type(JsonFieldType.STRING).description("유저 프로필사진"),
                                fieldWithPath("profile.rating").type(JsonFieldType.NUMBER).description("유저 평점"),
                                fieldWithPath("items").type(JsonFieldType.ARRAY).description("입찰에 사용된 아이템 목록"),
                                fieldWithPath("items.[].itemId").type(JsonFieldType.NUMBER).description("물건 ID"),
                                fieldWithPath("items.[].title").type(JsonFieldType.STRING).description("아이템 제목"),
                                fieldWithPath("items.[].description").type(JsonFieldType.STRING).description("아이템 설명"),
                                fieldWithPath("items.[].imageUrls").type(JsonFieldType.ARRAY).description("물건의 첫번째이미지(요청상배열)"),
                                fieldWithPath("items.[].createdAt").type(JsonFieldType.NULL).description("아이템 생성 시간")
                        )
                ));
    }

    @Test
    @WithMockCustomUser
    @Description("입찰을 수정하는 기능")
    void updateBid() throws Exception {
        //given
        BidUpdateDTO bidUpdateDTO = bidStubData.getBidUpdateDTO();
        String content = objectMapper.writeValueAsString(bidUpdateDTO);

        BidUpdateResponseDTO bidUpdateResponseDTO = bidStubData.getBidUpdateResponseDTO();
        given(bidService.updateBid(Mockito.any(User.class), Mockito.anyInt(), Mockito.any(BidUpdateDTO.class))).willReturn(bidUpdateResponseDTO);
        //when

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.put(BASE_URL + "/bids/{bidId}", BID_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
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
                                parameterWithName("bidId").description("입찰 ID")
                        ),
                        requestFields(
                                fieldWithPath("itemIds").type(JsonFieldType.ARRAY).description("입찰 ID")
                        ),
                        responseFields(
                                fieldWithPath("bidId").type(JsonFieldType.NUMBER).description("입찰 ID"),
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 ID"),
                                fieldWithPath("exchangePostId").type(JsonFieldType.NUMBER).description("교환 게시글 ID"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("입찰 상태"),
                                fieldWithPath("itemDetails").type(JsonFieldType.ARRAY).description("입찰에 사용된 아이템의 상세 정보 리스트"),
                                fieldWithPath("itemDetails.[].itemId").type(JsonFieldType.NUMBER).description("아이템 ID"),
                                fieldWithPath("itemDetails.[].title").type(JsonFieldType.STRING).description("아이템 제목"),
                                fieldWithPath("itemDetails.[].description").type(JsonFieldType.STRING).description("아이템 설명"),
                                fieldWithPath("itemDetails.[].imageUrls").type(JsonFieldType.ARRAY).description("아이템 이미지 URL 리스트")
                        )
                ));

    }

    @Test
    @WithMockCustomUser
    @Description("입찰을 삭제하는 기능")
    void deleteBid() throws Exception {
        //given

        //when
        doNothing().when(bidService).deleteBid(Mockito.anyInt(),Mockito.any(User.class));
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.delete(BASE_URL+"/bids/{bidId}", BID_ID)
                        .header("Authorization", "Bearer yourAccessToken")
        );
        verify(bidService, times(ONE_ACTION)).deleteBid(Mockito.anyInt(),Mockito.any(User.class));
        //then
        result
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("bidId").description("입찰 ID")
                        )
                ));
    }

    @Test
    @WithMockCustomUser
    @Description("거래를 완료하는 기능")
    void completeExchange() throws Exception {
        //given

        //when
        doNothing().when(bidService).completeExchange(Mockito.anyInt(),Mockito.anyInt(),Mockito.any(User.class));
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.put(BASE_URL+"/{exchangePostId}/bids/{bidId}/complete", EXCHANGE_POST_ID,BID_ID)
                        .header("Authorization", "Bearer yourAccessToken")
        );
        //then

        result
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("exchangePostId").description("게시글 ID"),
                                parameterWithName("bidId").description("입찰 ID")
                        )
                ));
    }

    @Test
    @WithMockCustomUser
    @Description("거래를 예약하는 기능")
    void reserveExchange() throws Exception {
        //given

        //when
        doNothing().when(bidService).toggleReserveExchange(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt());
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.put(BASE_URL+"/{exchangePostId}/bids/{bidId}/reserve", EXCHANGE_POST_ID,BID_ID)
                        .header("Authorization", "Bearer yourAccessToken")
        );
        //then

        result
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("exchangePostId").description("게시글 ID"),
                                parameterWithName("bidId").description("입찰 ID")
                        )
                ));
    }

    @Test
    @WithMockCustomUser
    @Description("입찰 거절")
    void denyBid() throws Exception {
        //given

        //when
        doNothing().when(bidService).toggleReserveExchange(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt());
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.put(BASE_URL+"/{exchangePostId}/bids/{bidId}/deny", EXCHANGE_POST_ID,BID_ID)
                        .header("Authorization", "Bearer yourAccessToken")
        );
        //then

        result
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("exchangePostId").description("게시글 ID"),
                                parameterWithName("bidId").description("입찰 ID")
                        )
                ));
    }

    @Test
    @WithMockCustomUser
    @Description("입찰 거절 취소")
    void undoDenyBid() throws Exception {
        //given

        //when
        doNothing().when(bidService).toggleReserveExchange(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt());
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.put(BASE_URL+"/{exchangePostId}/bids/{bidId}/undo-deny", EXCHANGE_POST_ID,BID_ID)
                        .header("Authorization", "Bearer yourAccessToken")
        );
        //then

        result
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("exchangePostId").description("게시글 ID"),
                                parameterWithName("bidId").description("입찰 ID")
                        )
                ));
    }

    @Test
    @WithMockCustomUser
    @Description("거절된 입찰 목록 조회")
    void getDeniedBidsForPost() throws Exception {
        //given
        BidListResponseDTO bidListResponseDTO = bidStubData.getBidListResponseDTO();
        List<BidListResponseDTO> bidListResponseDTOS = new ArrayList<>();
        bidListResponseDTOS.add(bidListResponseDTO);

        given(bidService.findDeniedBidsForPost(Mockito.anyInt(), Mockito.anyInt())).willReturn(bidListResponseDTOS);
        //when

        //then

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + "/{exchangePostId}/bids/denied", EXCHANGE_POST_ID)
                .header("Authorization", "Bearer yourAccessToken"));

        //then

        result.andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("exchangePostId").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].isOwner").type(JsonFieldType.BOOLEAN).description("교환 게시글 작성자인지를 체크"),
                                fieldWithPath("[].items").type(JsonFieldType.ARRAY).description("입찰에 사용되는 아이템의 상세 정보"),
                                fieldWithPath("[].items.[].title").type(JsonFieldType.STRING).description("아이템 제목"),
                                fieldWithPath("[].items.[].description").type(JsonFieldType.STRING).description("아이템 설명"),
                                fieldWithPath("[].items.[].imgUrl").type(JsonFieldType.STRING).description("아이템의 첫 번째 이미지 URL"),
                                fieldWithPath("[].items.[].createdAt").type(JsonFieldType.STRING).description("아이템 생성 시간")
                        )
                ));
    }
}