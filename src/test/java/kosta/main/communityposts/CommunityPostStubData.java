package kosta.main.communityposts;

import kosta.main.communityposts.dto.*;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.likes.entity.Like;
import kosta.main.users.UserStubData;
import kosta.main.users.entity.User;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommunityPostStubData {

    public static final int COMMUNITY_POST_ID = 1;
    public static final String TITLE = "CommunityPost제목";
    public static final String CONTENT = "CommunityPost내용";
    public static final int VIEWS = 20;
    public static final int LIKE_ID = 1;
    public static final int ANOTHER_LIKE_ID = 2;
    public static final int ANOTHER_COMMUNITY_POST_ID = 2;
    public static final String ANOTHER_TITLE = "제에에목";
    public static final String ANOTHER_CONTENT = "내애애용";
    public static final int USER_ID = 1;
    private UserStubData userStubData;
    public CommunityPost getCommunityPost(){
        userStubData = new UserStubData();
        User user = userStubData.getUser();
        CommunityPost communityPost = CommunityPost.builder()
                .communityPostId(COMMUNITY_POST_ID)
                .user(user)
                .title(TITLE)
                .content(CONTENT)
                .views(VIEWS)
                .build();
        Like like = Like.builder()
                .likeId(LIKE_ID)
                .user(user)
                .communityPost(communityPost)
                .build();
        communityPost.updateLikes(like);
        return communityPost;
    }
    public CommunityPost getAnotherCommunityPost(){
        userStubData = new UserStubData();
        User anotherUser = userStubData.getUpdateUser();
        CommunityPost communityPost = CommunityPost.builder()
                .communityPostId(ANOTHER_COMMUNITY_POST_ID)
                .user(anotherUser)
                .title(ANOTHER_TITLE)
                .content(ANOTHER_CONTENT)
                .views(VIEWS)
                .build();
        Like like = Like.builder()
                .likeId(ANOTHER_LIKE_ID)
                .user(anotherUser)
                .communityPost(communityPost)
                .build();
        communityPost.updateLikes(like);
        return communityPost;
    }

    public List<CommunityPostListDto> getCommunityPostListDto(){
        CommunityPost communityPost = getCommunityPost();
        CommunityPost anotherCommunityPost = getAnotherCommunityPost();
        List<CommunityPostListDto> communityPostLists = new ArrayList<>();
        communityPostLists.add(CommunityPostListDto.from(communityPost));
        communityPostLists.add(CommunityPostListDto.from(anotherCommunityPost));
        return communityPostLists;
    }

    public CommunityPostResponseDto getCommunityPostResponseDto(){
        CommunityPost communityPost = getCommunityPost();
        return CommunityPostResponseDto.of(communityPost);
    }

    public CommunityPostUpdateDto getCommunityPostUpdateDto(){
        CommunityPost communityPost = getCommunityPost();
        return CommunityPostUpdateDto.builder()
                .title(communityPost.getTitle())
                .userId(communityPost.getUser().getUserId())
                .content(communityPost.getContent())
                .build();
    }
    public CommunityPostDetailDto getCommunityPostDetailDto(){
        CommunityPost communityPost = getCommunityPost();
        return CommunityPostDetailDto.from(communityPost);
    }
    public CommunityPostCreateDto getCommunityPostCreateDto(){
        return CommunityPostCreateDto.builder()
                .userId(USER_ID)
                .title(TITLE)
                .content(CONTENT)
                .build();
    }

    public MockMultipartFile getMockMultipartFile() throws IOException {
        final String fileName = "testImage1"; //파일명
        final String contentType = "png"; //파일타입
        final String filePath = "src/test/resources/testImage/"+fileName+"."+contentType; //파일경로
        FileInputStream fileInputStream = new FileInputStream(filePath);

        //Mock파일생성
        MockMultipartFile image1 = new MockMultipartFile(
                "file", //name
                fileName + "." + contentType, //originalFilename
                contentType,
                fileInputStream
        );

        return image1;
    }


}
