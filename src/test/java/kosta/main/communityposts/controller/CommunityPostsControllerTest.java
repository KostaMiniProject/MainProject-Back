package kosta.main.communityposts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kosta.main.communityposts.CommunityPostStubData;
import kosta.main.communityposts.dto.*;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.communityposts.service.CommunityPostsService;
import kosta.main.global.annotation.WithMockCustomUser;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


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
    private final String BASE_URL = "/community-posts";

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
        communityPostStubData = new CommunityPostStubData();
             MockMvcBuilders.webAppContextSetup(webApplicationContext)
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
        List<CommunityPostListDto> communityPostListDto = communityPostStubData.getCommunityPostListDto();
        // when
        when(communityPostsService.findPosts()).thenReturn(communityPostListDto);

        // then
        this.mockMvc.perform(get(BASE_URL))
                .andDo(print())
                .andExpect(status().isOk());
//                .andDo(restDocs.document());
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
        this.mockMvc.perform(get(BASE_URL+"/1"))
                .andDo(print())
                .andExpect(status().isOk());
//                .andDo(restDocs.document())
    }

    @Test
    @DisplayName("커뮤니티 게시글 작성 성공테스트")
    @WithMockCustomUser
    void addPost() throws Exception {
        //Given
        CommunityPost communityPost = communityPostStubData.getCommunityPost();
        CommunityPostCreateDto communityPostCreateDto1 = communityPostStubData.getCommunityPostCreateDto();
        String content = objectMapper.writeValueAsString(communityPostCreateDto1);
        MockMultipartFile file = communityPostStubData.getMockMultipartFile();

        MockPart communityPostCreateDto = new MockPart("communityPostCreateDto", content.getBytes(StandardCharsets.UTF_8));
        communityPostCreateDto.getHeaders().setContentType(APPLICATION_JSON);
        given(communityPostsService.addPost(Mockito.any(CommunityPostCreateDto.class), Mockito.anyList())).willReturn(communityPost);
        //When

        //Then
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.multipart(HttpMethod.POST,BASE_URL)
                        .file(file)
                        .part(communityPostCreateDto)
                .with(csrf()));


        perform
                .andDo(print())
                .andExpect(status().isOk());
//                .andDo(restDocs.document());
    }

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
                MockMvcRequestBuilders.multipart(HttpMethod.PUT,BASE_URL+"/{communityPostId}", COMMUNITYPOST_ID)
                        .file(file)
                        .part(communityPostUpdateDto1)
                        .with(csrf()));


        perform
                .andDo(print())
                .andExpect(status().isOk());
//                .andDo(restDocs.document());
    }

    @Test
    @DisplayName("커뮤니티게시글 삭제 성공 테스트")
    @WithMockCustomUser
    void deletePost() throws Exception {
        //given

        //when
        doNothing().when(communityPostsService).deletePost(Mockito.anyInt());
        ResultActions actions = mockMvc.perform(
                delete(BASE_URL+"/{communityPostId}", COMMUNITYPOST_ID)
                        .with(csrf()));
        verify(communityPostsService, times(ONE_ACTION)).deletePost(Mockito.anyInt());
        //then
        actions
                .andDo(print())
                .andExpect(status().isOk());
    }

}