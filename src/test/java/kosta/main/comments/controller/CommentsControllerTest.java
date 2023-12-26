package kosta.main.comments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kosta.main.ControllerTest;
import kosta.main.comments.dto.CommentCreateDTO;
import kosta.main.comments.dto.CommentDTO;
import kosta.main.comments.dto.CommentParentDTO;
import kosta.main.comments.dto.CommentUpdateDTO;
import kosta.main.comments.service.CommentsService;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;


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
@WebMvcTest(CommentsController.class)
@MockBean(JpaMetamodelMappingContext.class)
class CommentsControllerTest extends ControllerTest {


    public static final String BASIC_URL = "/api/community-posts";
    public static final int COMMUNITY_POST_ID = 1;
    public static final int COMMENT_ID = 1;
    public static final int ONE_ACTION = 1;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestDocumentationResultHandler restDocs;

    @MockBean
    private CommentsService commentsService;

    private CommentStubData commentStubData;

    @BeforeEach
    public void setup() {
        commentStubData = new CommentStubData();
    }


    @Test
    @WithMockCustomUser
    @DisplayName("커뮤니티 게시글의 댓글을 전부 가져오기 성공 테스트")
    void findComments() throws Exception {
        //given
        List<CommentParentDTO> commentListDTO = commentStubData.getCommentListDTO();
        given(commentsService.findCommentsByPostId(Mockito.anyInt(),Mockito.any(User.class))).willReturn(commentListDTO);
        //when & then
        mockMvc.perform(get(BASIC_URL + "/{communityPostId}/comments", COMMUNITY_POST_ID)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("communityPostId").description("커뮤니티 게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].commentId").type(JsonFieldType.NUMBER).description("댓글 ID"),
                                fieldWithPath("[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("[].date").type(JsonFieldType.NULL).description("날짜"),
                                fieldWithPath("[].profile").type(JsonFieldType.OBJECT).description("유저 정보를 담고있는 객체"),
                                fieldWithPath("[].profile.userId").type(JsonFieldType.NUMBER).description("유저 Id"),
                                fieldWithPath("[].profile.name").type(JsonFieldType.STRING).description("유저이름"),
                                fieldWithPath("[].profile.imageUrl").type(JsonFieldType.STRING).description("유저 프로필 사진"),
                                fieldWithPath("[].children").type(JsonFieldType.ARRAY).description("자식 댓글을 담고있는 배열"),
                                fieldWithPath("[].children.[].commentId").type(JsonFieldType.NUMBER).description("댓글 ID"),
                                fieldWithPath("[].children.[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("[].children.[].profile").type(JsonFieldType.OBJECT).description("유저 정보를 담고있는 객체"),
                                fieldWithPath("[].children.[].profile.userId").type(JsonFieldType.NUMBER).description("유저 Id"),
                                fieldWithPath("[].children.[].profile.name").type(JsonFieldType.STRING).description("유저이름"),
                                fieldWithPath("[].children.[].profile.imageUrl").type(JsonFieldType.STRING).description("유저 프로필 사진"),
                                fieldWithPath("[].commentId").type(JsonFieldType.NUMBER).description("댓글 ID"),
                                fieldWithPath("[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("[].isOwner").type(JsonFieldType.BOOLEAN).description("댓글 주인여부"),
                                fieldWithPath("[].profile").type(JsonFieldType.OBJECT).description("유저 정보를 담고있는 객체"),
                                fieldWithPath("[].profile.userId").type(JsonFieldType.NUMBER).description("유저 Id"),
                                fieldWithPath("[].profile.name").type(JsonFieldType.STRING).description("유저이름"),
                                fieldWithPath("[].profile.imageUrl").type(JsonFieldType.STRING).description("유저 프로필 사진"),
                                fieldWithPath("[].children").type(JsonFieldType.ARRAY).description("자식 댓글을 담고있는 배열"),
                                fieldWithPath("[].children.[].commentId").type(JsonFieldType.NUMBER).description("댓글 ID"),
                                fieldWithPath("[].children.[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("[].children.[].isOwner").type(JsonFieldType.BOOLEAN).description("댓글 주인 여부"),
                                fieldWithPath("[].children.[].profile").type(JsonFieldType.OBJECT).description("유저 정보를 담고있는 객체"),
                                fieldWithPath("[].children.[].profile.userId").type(JsonFieldType.NUMBER).description("유저 Id"),
                                fieldWithPath("[].children.[].profile.name").type(JsonFieldType.STRING).description("유저이름"),
                                fieldWithPath("[].children.[].profile.imageUrl").type(JsonFieldType.STRING).description("유저 프로필 사진")
                        )

                ));

    }

    @Test
    @WithMockCustomUser
    @DisplayName("댓글 추가 성공 테스트")
    void addComment() throws Exception {
        //given
        CommentDTO commentDTO = commentStubData.getCommentDTO();
        CommentCreateDTO commentCreateDTO = commentStubData.getCommentCreateDTO();
        String content = objectMapper.writeValueAsString(commentCreateDTO);
        given(commentsService.addComment(Mockito.any(User.class), Mockito.anyInt(), Mockito.any(CommentCreateDTO.class))).willReturn(commentDTO);
        //when
        ResultActions result = mockMvc.perform(post(BASIC_URL + "/{communityPostId}/comments", COMMUNITY_POST_ID)
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
                        pathParameters(
                                parameterWithName("communityPostId").description("커뮤니티 게시글 ID")
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글의 내용"),
                                fieldWithPath("parentId").type(JsonFieldType.NUMBER).description("부모 댓글 ID(부모댓글이 없는경우 비워주세요)")
                        ),
                        responseFields(
                                fieldWithPath("communityPostId").type(JsonFieldType.NUMBER).description("커뮤니티 게시글 ID"),
                                fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("댓글의 ID"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글의 내용"),
                                fieldWithPath("date").type(JsonFieldType.STRING).description("댓글 작성일")
                        )

                ));

    }

    @Test
    @WithMockCustomUser
    @DisplayName("댓글 수정 성공 테스트")
    void updateComment() throws Exception {
        //given
        CommentUpdateDTO commentUpdateDTO = commentStubData.getCommentUpdateDTO();
        CommentDTO commentDTO = commentStubData.getCommentDTO();
        String content = objectMapper.writeValueAsString(commentUpdateDTO);
        given(commentsService.updateComment(Mockito.any(User.class), Mockito.anyInt(), Mockito.any(CommentUpdateDTO.class))).willReturn(commentDTO);

        //when
        ResultActions result = mockMvc.perform(put(BASIC_URL + "/comments/{commentId}", COMMENT_ID)
                .header("Authorization", "Bearer yourAccessToken")
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("commentId").description("댓글 ID")
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글의 내용")
                        ),
                        responseFields(
                                fieldWithPath("communityPostId").type(JsonFieldType.NUMBER).description("커뮤니티 게시글 ID"),
                                fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("댓글의 ID"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글의 내용"),
                                fieldWithPath("date").type(JsonFieldType.STRING).description("댓글 작성일")

                        )
                        ));

    }

    @Test
    @WithMockCustomUser
    @DisplayName("댓글 삭제 성공 테스트")
    void deleteComment() throws Exception {
        //given

        doNothing().when(commentsService).deleteComment(Mockito.anyInt(),Mockito.any(User.class));
        //when

        ResultActions result = mockMvc.perform(delete(BASIC_URL + "/comments/{commentId}", COMMENT_ID)
                .header("Authorization", "Bearer yourAccessToken")
                );

        //then
        verify(commentsService, times(ONE_ACTION)).deleteComment(Mockito.anyInt(),Mockito.any(User.class));

        result.andDo(print())
                .andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        )
                ));
    }
}