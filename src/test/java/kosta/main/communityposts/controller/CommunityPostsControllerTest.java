package kosta.main.communityposts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kosta.main.ControllerTest;
import kosta.main.communityposts.CommunityPostStubData;
import kosta.main.communityposts.dto.*;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.communityposts.service.CommunityPostsService;
import kosta.main.global.annotation.WithMockCustomUser;
import kosta.main.likes.dto.LikeDTO;
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
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpMethod;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.generate.RestDocumentationGenerator;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;


@ExtendWith({SpringExtension.class})
@WebMvcTest(CommunityPostsController.class)
@MockBean(JpaMetamodelMappingContext.class)
class CommunityPostsControllerTest extends ControllerTest {

    public static final int COMMUNITYPOST_ID = 1;
    public static final int ONE_ACTION = 1;
    @Autowired
    private RestDocumentationResultHandler restDocs;
    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private CommunityPostsService communityPostsService;

    private CommunityPostStubData communityPostStubData;
    private final String BASE_URL = "/api/community-posts";

    @BeforeEach
    public void setup() {
        communityPostStubData = new CommunityPostStubData();
    }
    @Test
    @WithMockCustomUser
    @DisplayName("게시글 목록 조회 성공 테스트")
    void findPosts() throws Exception {
        // given

        Page<CommunityPostDetailDTO> communityPostListDTOPage = communityPostStubData.getCommunityPostDetailDTOPage();

        // when
        when(communityPostsService.findPosts(Mockito.any(Pageable.class),Mockito.any(User.class))).thenReturn(communityPostListDTOPage);

        // then
        mockMvc.perform(get(BASE_URL)
                        .param("page","0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("page").description("현재 페이지를 표시하는 파라미터")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("커뮤니티게시글을 감싸고 있는 배열"),
                                fieldWithPath("data.[].communityPostId").type(JsonFieldType.NUMBER).description("커뮤니티게시글 ID"),
                                fieldWithPath("data.[].postOwner").type(JsonFieldType.BOOLEAN).description("게시글 소유주 여부"),
                                fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("커뮤니티게시글 제목"),
                                fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("커뮤니티게시글 내용"),
                                fieldWithPath("data.[].user").type(JsonFieldType.OBJECT).description("게시글 유저 정보를 담고있는 객체"),
                                fieldWithPath("data.[].user.userId").type(JsonFieldType.NUMBER).description("유저 ID"),
                                fieldWithPath("data.[].user.email").type(JsonFieldType.STRING).description("유저 이메일"),
                                fieldWithPath("data.[].user.name").type(JsonFieldType.STRING).description("유저 이름"),
                                fieldWithPath("data.[].user.address").type(JsonFieldType.STRING).description("유저 주소"),
                                fieldWithPath("data.[].user.phone").type(JsonFieldType.STRING).description("유저 전화번호"),
                                fieldWithPath("data.[].user.rating").type(JsonFieldType.NUMBER).description("유저 평점"),
                                fieldWithPath("data.[].user.profileImage").type(JsonFieldType.STRING).description("유저 프로필 이미지"),
                                fieldWithPath("data.[].communityPostStatus").type(JsonFieldType.STRING).description("커뮤니티게시글 상태(PUBLIC, PRIVATE, REPORTED, DELETED)"),
                                fieldWithPath("data.[].isPressLike").type(JsonFieldType.BOOLEAN).description("게시글들을 조회하는 유저가 좋아요를 눌렀는지 여부"),
                                fieldWithPath("data.[].commentCount").type(JsonFieldType.NUMBER).description("댓글 개수"),
                                fieldWithPath("data.[].imageUrl").type(JsonFieldType.ARRAY).description("이미지 데이터"),
                                fieldWithPath("data.[].likeCount").type(JsonFieldType.NUMBER).description("커뮤니티게시글 좋아요 수"),
                                fieldWithPath("data.[].date").type(JsonFieldType.STRING).description("해당 날짜와 비교한 커뮤니티 게시글 생성일자(~일전등)"),
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
    @DisplayName("게시글 상세 내용 조회 성공 테스트")
    void findPost() throws Exception {
        // given
        CommunityPostDetailDTO communityPostDetailDto = communityPostStubData.getCommunityPostDetailDto();
        // when
        when(communityPostsService.findPost(Mockito.any(User.class),Mockito.anyInt())).thenReturn(communityPostDetailDto);

        // then
        mockMvc.perform(get(BASE_URL + "/{communityPostId}",COMMUNITYPOST_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("communityPostId").description("커뮤니티 게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("communityPostId").type(JsonFieldType.NUMBER).description("커뮤니티게시글 ID"),
                                fieldWithPath("postOwner").type(JsonFieldType.BOOLEAN).description("게시글 주인인지 확인여부(주인일 경우 true)"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("커뮤니티게시글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("커뮤니티게시글 내용"),
                                fieldWithPath("views").type(JsonFieldType.NUMBER).description("커뮤니티게시글 조회수"),
                                fieldWithPath("imageUrl").type(JsonFieldType.ARRAY).description("이미지 데이터"),
                                fieldWithPath("communityPostStatus").type(JsonFieldType.STRING).description("커뮤니티게시글 상태(PUBLIC, PRIVATE, REPORTED, DELETED)"),
                                fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("커뮤니티게시글 좋아요 수")
                        )
                ));
    }

    @Test
    @DisplayName("커뮤니티 게시글 작성 성공테스트")
    @WithMockCustomUser
    void addPost() throws Exception {
        //Given
        CommunityPost communityPost = communityPostStubData.getCommunityPost();
        CommunityPostCreateDTO communityPostCreateDTO = communityPostStubData.getCommunityPostCreateDTO();
        CommunityPostDTO communityPostDTO = communityPostStubData.getCommunityPostDTO();
        String content = objectMapper.writeValueAsString(communityPostCreateDTO);
        MockMultipartFile file = communityPostStubData.getMockMultipartFile();

        MockPart communityPostCreateDto = new MockPart("communityPostCreateDTO", content.getBytes(StandardCharsets.UTF_8));
        communityPostCreateDto.getHeaders().setContentType(APPLICATION_JSON);
        given(communityPostsService.addPost(Mockito.any(User.class),Mockito.any(CommunityPostCreateDTO.class), Mockito.anyList())).willReturn(communityPostDTO);
        //When

        //Then
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.multipart(HttpMethod.POST,BASE_URL)
                        .file(file)
                        .part(communityPostCreateDto)
                        .header("Authorization", "Bearer yourAccessToken")
                        );


        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        requestParts(
                                partWithName("communityPostCreateDTO").description("커뮤니티 게시글 생성 DTO"),
                                partWithName("file").description("커뮤니티 게시글에 올라갈사진(여러장 가능)")
                        ),
                        requestPartFields("communityPostCreateDTO",
                                fieldWithPath("title").type(JsonFieldType.STRING).description("커뮤니티게시글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("커뮤니티게시글 내용")
                        ),
                        responseFields(
                                fieldWithPath("communityPostId").type(JsonFieldType.NUMBER).description("커뮤니티게시글 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("커뮤니티게시글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("커뮤니티게시글 내용"),
                                fieldWithPath("views").type(JsonFieldType.NUMBER).description("조회수"),
                                fieldWithPath("imagePaths").type(JsonFieldType.ARRAY).description("이미지 주소 경로")
                                )
                ));
    }

    @Test
    @DisplayName("커뮤니티 게시글 수정 성공 테스트")
    @WithMockCustomUser
    void updatePost() throws Exception {
        //Given
        CommunityPostUpdateDTO communityPostUpdateDTO = communityPostStubData.getCommunityPostUpdateDTO();
        CommunityPostResponseDTO CommunityPostResponseDTO = communityPostStubData.getCommunityPostResponseDTO();
        String content = objectMapper.writeValueAsString(communityPostUpdateDTO);
        MockMultipartFile file = communityPostStubData.getMockMultipartFile();

        MockPart communityPostUpdateDto1 = new MockPart("communityPostUpdateDTO", content.getBytes(StandardCharsets.UTF_8));
        communityPostUpdateDto1.getHeaders().setContentType(APPLICATION_JSON);
        given(communityPostsService.updatePost(Mockito.any(User.class),Mockito.anyInt(),Mockito.any(CommunityPostUpdateDTO.class), Mockito.anyList())).willReturn(CommunityPostResponseDTO);
        //When

        //Then
        ResultActions perform = mockMvc.perform(
                multipart(HttpMethod.PUT,BASE_URL+"/{communityPostId}", COMMUNITYPOST_ID)
                        .file(file)
                        .part(communityPostUpdateDto1)
                        .header("Authorization", "Bearer yourAccessToken")
                        .requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, BASE_URL+"/{communityPostId}"));


        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("communityPostId").description("커뮤니티 게시글 ID")
                        ),
                        requestParts(
                                partWithName("communityPostUpdateDTO").description("커뮤니티 게시글 업데이트 정보DTO"),
                                partWithName("file").description("커뮤니티 게시글에 올라갈사진(여러장 가능)")
                        ),
                        requestPartFields("communityPostUpdateDTO",
                                fieldWithPath("title").type(JsonFieldType.STRING).description("커뮤니티게시글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("커뮤니티게시글 내용"),
                                fieldWithPath("imagePaths").type(JsonFieldType.ARRAY).description("이미지 저장경로(안넣어도 됩니다 내부 로직용임)").optional()
                                ),
                        responseFields(
                                fieldWithPath("user").type(JsonFieldType.OBJECT).description("유저에 대한 정보"),
                                fieldWithPath("user.email").type(JsonFieldType.STRING).description("유저의 아이디로 사용되는 이메일"),
                                fieldWithPath("user.name").type(JsonFieldType.STRING).description("유저의 이름"),
                                fieldWithPath("user.address").type(JsonFieldType.STRING).description("유저의 주소지"),
                                fieldWithPath("user.phone").type(JsonFieldType.STRING).description("유저의 전화번호"),
                                fieldWithPath("user.profileImage").type(JsonFieldType.STRING).description("유저의 프로필 이미지"),
                                fieldWithPath("user.userStatus").type(JsonFieldType.STRING).description("유저의 상태(ACTIVATE, BANNED ,DELETED)"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("커뮤니티게시글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("커뮤니티게시글 내용"),
                                fieldWithPath("views").type(JsonFieldType.NUMBER).description("커뮤니티게시글 조회수"),
                                fieldWithPath("communityPostStatus").type(JsonFieldType.STRING).description("커뮤니티게시글 상태(PUBLIC, PRIVATE, REPORTED, DELETED)"),
                                fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("커뮤니티게시글 좋아요 수")
                                )
                ));
    }

    @Test
    @DisplayName("커뮤니티게시글 삭제 성공 테스트")
    @WithMockCustomUser
    void deletePost() throws Exception {
        //given

        //when
        doNothing().when(communityPostsService).deletePost(Mockito.anyInt(),Mockito.any(User.class));
        ResultActions actions = mockMvc.perform(
                delete(BASE_URL+"/{communityPostId}", COMMUNITYPOST_ID)
                        .header("Authorization", "Bearer yourAccessToken")
                        );
        verify(communityPostsService, times(ONE_ACTION)).deletePost(Mockito.anyInt(),Mockito.any(User.class));
        //then
        actions
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        )
                ));
    }

//    TODO: 추후 구현
    @Test
    @WithMockCustomUser
    @DisplayName("커뮤니티게시글 좋아요 추가 성공 테스트")
    void toggleLikePost() throws Exception {
        //given
        LikeDTO likeDTO = communityPostStubData.getLikeDTO();
        given(communityPostsService.toggleLikePost(Mockito.anyInt(), Mockito.any(User.class))).willReturn(likeDTO);
        //when

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.put(BASE_URL + "/likes/{communityPostId}", COMMUNITYPOST_ID)
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
                                parameterWithName("communityPostId").description("커뮤니티 게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("likeId").type(JsonFieldType.NUMBER).description("좋아요 ID"),
                                fieldWithPath("communityPostId").type(JsonFieldType.NUMBER).description("커뮤니티 게시글 ID"),
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("유저 ID")
                        )
                ));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("커뮤니티게시글 좋아요 삭제 성공 테스트")
    void toggleLikePostDelete() throws Exception {
        //given
        CommunityPostLikeCancelledDTO communityPostLikeCancelledDTO
                = communityPostStubData.getCommunityPostLikeCancelledDTO();
        given(communityPostsService.toggleLikePost(Mockito.anyInt(), Mockito.any(User.class))).willReturn(communityPostLikeCancelledDTO);
        //when

        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.put(BASE_URL + "/likes/{communityPostId}", COMMUNITYPOST_ID)
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
                                parameterWithName("communityPostId").description("커뮤니티 게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("좋아요 취소가 정상적으로 작동했다는 메세지를 보냄")
                        )
                ));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("게시글 목록 검색 성공 테스트")
    void search() throws Exception {
        // given

        Page<CommunityPostListDTO> communityPostListDTOPage = communityPostStubData.getCommunityPostListDTOPage();

        // when
        when(communityPostsService.search(Mockito.anyString(),Mockito.any(Pageable.class),Mockito.any(User.class))).thenReturn(communityPostListDTOPage);

        // then
        mockMvc.perform(get(BASE_URL+"/search")
                        .param("keyword","abcd")
                        .param("page","0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("keyword").description("검색 키워드를 입력하는 파라미터"),
                                parameterWithName("page").description("현재 페이지를 표시하는 파라미터")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("커뮤니티게시글을 감싸고 있는 배열"),
                                fieldWithPath("data.[].communityPostId").type(JsonFieldType.NUMBER).description("커뮤니티게시글 ID"),
                                fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("커뮤니티게시글 제목"),
                                fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("커뮤니티게시글 내용"),
                                fieldWithPath("data.[].views").type(JsonFieldType.NUMBER).description("커뮤니티게시글 조회수"),
                                fieldWithPath("data.[].imageUrl").type(JsonFieldType.ARRAY).description("이미지 데이터"),
                                fieldWithPath("data.[].communityPostStatus").type(JsonFieldType.STRING).description("커뮤니티게시글 상태(PUBLIC, PRIVATE, REPORTED, DELETED)"),
                                fieldWithPath("data.[].likeCount").type(JsonFieldType.NUMBER).description("커뮤니티게시글 좋아요 수"),
                                fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보를 감싸고 있는 배열"),
                                fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("현재 페이지 숫자"),
                                fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 크기(한 번에 몇개의 정보를 가져올지"),
                                fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("전체 데이터 개수"),
                                fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 숫자")
                        )
                ));
    }

}