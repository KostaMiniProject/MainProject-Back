package kosta.main.chatrooms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kosta.main.ControllerTest;
import kosta.main.chatrooms.dto.ChatRoomEnterResponseDTO;
import kosta.main.chatrooms.dto.ChatRoomResponseDTO;
import kosta.main.chatrooms.dto.CreateChatRoomDTO;
import kosta.main.chatrooms.dto.CreateChatRoomResponseDTO;
import kosta.main.chatrooms.service.ChatRoomService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({SpringExtension.class})
@WebMvcTest(ChatRoomsController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ChatRoomsControllerTest extends ControllerTest {

    public static final String BASIC_URL = "/api/chatRooms";
    public static final int CHAT_ROOM_ID = 1;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestDocumentationResultHandler restDocs;

    @MockBean
    private ChatRoomService chatRoomService;

    ChatRoomStubData chatRoomStubData;

    @BeforeEach
    public void setup() {
        chatRoomStubData = new ChatRoomStubData();
    }
    @Test
    @WithMockCustomUser
    @DisplayName("채팅방 생성하기")
    void createChatRoom() throws Exception {
        //given
        CreateChatRoomResponseDTO createChatRoomResponseDTO = chatRoomStubData.getCreateChatRoomResponseDTO();
        CreateChatRoomDTO createChatRoomDTO = chatRoomStubData.getCreateChatRoomDTO();
        String content = objectMapper.writeValueAsString(createChatRoomDTO);

        //when
        given(chatRoomService.createChatRoom(Mockito.any(CreateChatRoomDTO.class), Mockito.any(User.class))).willReturn(createChatRoomResponseDTO);
        ResultActions result = mockMvc.perform(post(BASIC_URL)
                .header("Authorization", "Bearer yourAccessToken")
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        result.andDo(print())
                .andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("bidId").type(JsonFieldType.NUMBER).description("입찰 ID번호")
                        ),
                        responseFields(
                                fieldWithPath("chatRoomId").type(JsonFieldType.NUMBER).description("채팅방 ID"),
                                fieldWithPath("exchangePostId").type(JsonFieldType.NUMBER).description("물물교환 게시글 ID"),
                                fieldWithPath("senderId").type(JsonFieldType.NUMBER).description("채팅 보내는 사람 ID"),
                                fieldWithPath("receiverId").type(JsonFieldType.NUMBER).description("채팅 받는 사람 ID"),
                                fieldWithPath("bidId").type(JsonFieldType.NUMBER).description("입찰 ID")
                        )
                ));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("채팅방 나가기")
    void leaveChatRoom() throws Exception {
        doNothing().when(chatRoomService).leaveChatRoom(Mockito.anyInt(),Mockito.any(User.class));
        //when

        ResultActions result = mockMvc.perform(delete(BASIC_URL + "/{chatRoomId}", CHAT_ROOM_ID)
                .header("Authorization", "Bearer yourAccessToken")
        );

        //then

        result.andDo(print())
                .andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("chatRoomId").description("채팅방 ID")
                        )
                ));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("채팅방 조회하기")
    void getChatRooms() throws Exception {
        //given
        List<ChatRoomResponseDTO> chatRoomResponseDTOs = chatRoomStubData.getChatRoomResponseDTOs();
        given(chatRoomService.getChatRooms(Mockito.any(User.class))).willReturn(chatRoomResponseDTOs);
        //when & then
        mockMvc.perform(get(BASIC_URL)
                        .header("Authorization", "Bearer yourAccessToken"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("[].chatRoomId").type(JsonFieldType.NUMBER).description("채팅방 ID"),
                                fieldWithPath("[].participantName").type(JsonFieldType.STRING).description("참여자 이름"),
                                fieldWithPath("[].lastMessageTimeDifference").type(JsonFieldType.STRING).description("마지막 메세지 시간과의 차이"),
                                fieldWithPath("[].lastMessageDateTime").type(JsonFieldType.STRING).description("실제 마지막 메시지의 시간을 저장하는 필드"),
                                fieldWithPath("[].lastMessageContent").type(JsonFieldType.STRING).description("마지막 메세지 내용"),
                                fieldWithPath("[].participantProfileImg").type(JsonFieldType.STRING).description("참가자 프로필 이미지"),
                                fieldWithPath("[].anotherParticipantName").type(JsonFieldType.STRING).description("다른 참여자 이름"),
                                fieldWithPath("[].anotherParticipantProfileImg").type(JsonFieldType.STRING).description("다른 참여자 프로필 이미지"),
                                fieldWithPath("[].exchangePostAddress").type(JsonFieldType.STRING).description("교환 주소")
                        )

                ));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("해당 채팅방 채팅내용 가지고오기")
    void getChatList() throws Exception {
        //given
        ChatRoomEnterResponseDTO chatRoomEnterResponseDTO = chatRoomStubData.getChatRoomEnterResponseDTO();
        given(chatRoomService.getChatList(Mockito.anyInt(), Mockito.any(User.class), Mockito.any(Pageable.class))).willReturn(chatRoomEnterResponseDTO);

        //when
        ResultActions result = mockMvc.perform(get(BASIC_URL + "/{chatRoomId}", CHAT_ROOM_ID)
                .header("Authorization", "Bearer yourAccessToken"));

        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("chatRoomId").description("채팅방 ID")
                        ),
                        responseFields(
                                fieldWithPath("isOwner").type(JsonFieldType.BOOLEAN).description("채팅방 ID"),
                                fieldWithPath("exchangePostId").type(JsonFieldType.NUMBER).description("물물교환 게시글 ID"),
                                fieldWithPath("exchangePostTittle").type(JsonFieldType.STRING).description("물물교환 게시글 제목"),
                                fieldWithPath("exchangePostAddress").type(JsonFieldType.STRING).description("물물교환 게시글 주소"),
                                fieldWithPath("exchangePostCategory").type(JsonFieldType.STRING).description("물물교환 물건 카테고리"),
                                fieldWithPath("exchangePostImage").type(JsonFieldType.STRING).description("물물교환 게시글 이미지"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("입찰 상태"),
                                fieldWithPath("bidId").type(JsonFieldType.NUMBER).description("입찰 ID"),
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("상대방 유저의 ID"),
                                fieldWithPath("userName").type(JsonFieldType.STRING).description("상대방 유저의 닉네임"),
                                fieldWithPath("userProfileImage").type(JsonFieldType.STRING).description("상대방 유저의 이미지"),
                                fieldWithPath("messages").type(JsonFieldType.ARRAY).description("채팅 메시지 목록"),
                                fieldWithPath("messages.[].chatId").type(JsonFieldType.NUMBER).description("입찰 ID"),
                                fieldWithPath("messages.[].senderId").type(JsonFieldType.NUMBER).description("보낸 사람의 ID 본인 ID 일지도, 타인일수 도 있음"),
                                fieldWithPath("messages.[].content").type(JsonFieldType.STRING).description("채팅 보낸 내용"),
                                fieldWithPath("messages.[].imageUrl").type(JsonFieldType.STRING).description("이미지 주소"),
                                fieldWithPath("messages.[].createAt").type(JsonFieldType.STRING).description("채팅 생성시각"),
                                fieldWithPath("messages.[].isRead").type(JsonFieldType.BOOLEAN).description("읽었는지 여부")
                        )

                        ));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("채팅방 전체에 알리기??")
    void notifyChatRoomEntry() throws Exception {
        //given

        doNothing().when(chatRoomService).notifyChatRoomEntry(Mockito.anyInt(),Mockito.anyInt());
        //when
        ResultActions result = mockMvc.perform(post(BASIC_URL + "/{chatRoomId}/notify-entry", CHAT_ROOM_ID)
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
                                parameterWithName("chatRoomId").description("채팅방 ID")
                        )
                ));
    }
}