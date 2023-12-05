package kosta.main.communityposts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kosta.main.communityposts.CommunityPostStubData;
import kosta.main.communityposts.dto.*;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.communityposts.service.CommunityPostsService;
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
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

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


@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(CommunityPostsController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(kosta.main.RestDocsConfiguration.class)
class CommunityPostsControllerTest {

    public static final int COMMUNITYPOST_ID = 1;
    public static final int ONE_ACTION = 1;
    @Autowired
    private RestDocumentationResultHandler restDocs;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommunityPostsService communityPostsService;

    private CommunityPostStubData communityPostStubData;
    private final String BASE_URL = "/api/community-posts";

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
        communityPostStubData = new CommunityPostStubData();
             mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                    .apply(documentationConfiguration(restDocumentationContextProvider))
                    .alwaysDo(print())														// 이건 왜하는지 모르겠음.
                    .alwaysDo(restDocs)														// 재정의한 핸들러를 적용함. 적용하면 일반 document에도 적용됨. 일반 document로 선언되면 그부분도 같이 생성됨에 유의해야 함.
                    .addFilters(new CharacterEncodingFilter("UTF-8", true))					// 한글깨짐 방지 처리
                    .build();
    }
    @Test
    @WithMockCustomUser
    @DisplayName("게시글 목록 조회 성공 테스트")
    void findPosts() throws Exception {
        // given
        Page<CommunityPostListDto> communityPostListDtoPage = communityPostStubData.getCommunityPostListDtoPage();

        // when
        when(communityPostsService.findPosts(Mockito.any(Pageable.class))).thenReturn(communityPostListDtoPage);

        // then
        mockMvc.perform(get(BASE_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("커뮤니티게시글을 감싸고 있는 배열"),
                                fieldWithPath("data.[].communityPostId").type(JsonFieldType.NUMBER).description("커뮤니티게시글 ID"),
                                fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("커뮤니티게시글 제목"),
                                fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("커뮤니티게시글 내용"),
                                fieldWithPath("data.[].views").type(JsonFieldType.NUMBER).description("커뮤니티게시글 조회수"),
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

    @Test
    @WithMockCustomUser
    @DisplayName("게시글 상세 내용 조회 성공 테스트")
    void findPost() throws Exception {
        // given
        CommunityPostDetailDto communityPostDetailDto = communityPostStubData.getCommunityPostDetailDto();
        // when
        when(communityPostsService.findPost(Mockito.anyInt())).thenReturn(communityPostDetailDto);

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
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("유저 ID(고유숫자값)"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("커뮤니티게시글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("커뮤니티게시글 내용"),
                                fieldWithPath("views").type(JsonFieldType.NUMBER).description("커뮤니티게시글 조회수"),
                                fieldWithPath("communityPostStatus").type(JsonFieldType.STRING).description("커뮤니티게시글 상태(PUBLIC, PRIVATE, REPORTED, DELETED)"),
                                fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("커뮤니티게시글 좋아요 수")
                        )
                ));
    }

//    @Test  엔티티 반환이라 일정기간 보류
//    @DisplayName("커뮤니티 게시글 작성 성공테스트")
//    @WithMockCustomUser
//    void addPost() throws Exception {
//        //Given
//        CommunityPost communityPost = communityPostStubData.getCommunityPost();
//        CommunityPostCreateDto communityPostCreateDto1 = communityPostStubData.getCommunityPostCreateDto();
//        String content = objectMapper.writeValueAsString(communityPostCreateDto1);
//        MockMultipartFile file = communityPostStubData.getMockMultipartFile();
//
//        MockPart communityPostCreateDto = new MockPart("communityPostCreateDto", content.getBytes(StandardCharsets.UTF_8));
//        communityPostCreateDto.getHeaders().setContentType(APPLICATION_JSON);
//        given(communityPostsService.addPost(Mockito.any(CommunityPostCreateDto.class), Mockito.anyList())).willReturn(communityPost);
//        //When
//
//        //Then
//        ResultActions perform = mockMvc.perform(
//                MockMvcRequestBuilders.multipart(HttpMethod.POST,BASE_URL)
//                        .file(file)
//                        .part(communityPostCreateDto)
//                .with(csrf()));
//
//
//        perform
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(restDocs.document(
//                        requestParts(
//                                partWithName("communityPostCreateDto").description("유저 업데이트 정보"),
//                                partWithName("file").description("유저 프로필 사진")
//                        ),
//                        requestPartFields("communityPostCreateDto",
//                                fieldWithPath("title").type(JsonFieldType.STRING).description("커뮤니티게시글 제목"),
//                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("유저 ID(고유숫자값)"),
//                                fieldWithPath("content").type(JsonFieldType.STRING).description("커뮤니티게시글 내용")
//                        ),
//                        responseFields(
//                                fieldWithPath("communityPostId").type(JsonFieldType.NUMBER).description("커뮤니티게시글 ID"),
//
//                                )
//                ));
//    }

    @Test
    @DisplayName("커뮤니티 게시글 수정 성공 테스트")
    @WithMockCustomUser
    void updatePost() throws Exception {
        //Given
        CommunityPostUpdateDto communityPostUpdateDto = communityPostStubData.getCommunityPostUpdateDto();
        CommunityPostResponseDto communityPostResponseDto = communityPostStubData.getCommunityPostResponseDto();
        String content = objectMapper.writeValueAsString(communityPostUpdateDto);
        MockMultipartFile file = communityPostStubData.getMockMultipartFile();

        MockPart communityPostUpdateDto1 = new MockPart("communityPostUpdateDto", content.getBytes(StandardCharsets.UTF_8));
        communityPostUpdateDto1.getHeaders().setContentType(APPLICATION_JSON);
        given(communityPostsService.updatePost(Mockito.anyInt(),Mockito.any(CommunityPostUpdateDto.class), Mockito.anyList())).willReturn(communityPostResponseDto);
        //When

        //Then
        ResultActions perform = mockMvc.perform(
                multipart(HttpMethod.PUT,BASE_URL+"/{communityPostId}", COMMUNITYPOST_ID)
                        .file(file)
                        .part(communityPostUpdateDto1)
                        .with(csrf())
                        .requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, BASE_URL+"/{communityPostId}"));


        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("communityPostId").description("커뮤니티 게시글 ID")
                        ),
                        requestParts(
                                partWithName("communityPostUpdateDto").description("유저 업데이트 정보"),
                                partWithName("file").description("유저 프로필 사진")
                        ),
                        requestPartFields("communityPostUpdateDto",
                                fieldWithPath("title").type(JsonFieldType.STRING).description("커뮤니티게시글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("커뮤니티게시글 내용"),
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("유저 ID(고유숫자값)"),
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
                        .with(csrf()));
        verify(communityPostsService, times(ONE_ACTION)).deletePost(Mockito.anyInt(),Mockito.any(User.class));
        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        )
                ));
    }

}