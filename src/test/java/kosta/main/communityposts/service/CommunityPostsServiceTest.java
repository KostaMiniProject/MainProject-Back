package kosta.main.communityposts.service;

import kosta.main.comments.repository.CommentsRepository;
import kosta.main.communityposts.CommunityPostStubData;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.communityposts.repository.CommunityPostsRepository;
import kosta.main.global.s3upload.service.ImageService;
import kosta.main.likes.repository.LikesRepository;
import kosta.main.users.UserStubData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class CommunityPostsServiceTest {

    public static final int COMMUNITY_POST_ID = 1;
    @InjectMocks
    CommunityPostsService communityPostsService;

    @Mock
    private CommunityPostsRepository communityPostsRepository;
    @Mock
    private LikesRepository likesRepository;
    @Mock
    private CommentsRepository commentsRepository;
    @Mock
    private ImageService imageService;

    private CommunityPostStubData communityPostStubData;

    @BeforeEach
    void init(){
        communityPostStubData = new CommunityPostStubData();
    }
    @Test
    void findCommunityPostByCommunityPostId() {
        //given

        CommunityPost communityPost = communityPostStubData.getCommunityPost();

        given(communityPostsRepository.findById(Mockito.anyInt())).willReturn(Optional.of(communityPost));

        //when

        CommunityPost result = communityPostsService.findCommunityPostByCommunityPostId(COMMUNITY_POST_ID);

        //then

        Assertions.assertThat(result.getCommunityPostId()).isEqualTo(communityPost.getCommunityPostId());
        Assertions.assertThat(result.getContent()).isEqualTo(communityPost.getContent());
        Assertions.assertThat(result.getTitle()).isEqualTo(communityPost.getTitle());

    }

    @Test
    void findCommentByCommentId() {


    }

    @Test
    void findPosts() {
    }

    @Test
    void findPost() {
    }

    @Test
    void addPost() {
    }

    @Test
    void updatePost() {
    }

    @Test
    void deletePost() {
    }

    @Test
    void toggleLikePost() {
    }

    @Test
    void findCommentsByPostId() {
    }

    @Test
    void addComment() {
    }

    @Test
    void updateComment() {
    }

    @Test
    void deleteComment() {
    }
}