package kosta.main.communityposts;

import kosta.main.communityposts.dto.*;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.likes.entity.Like;
import kosta.main.users.UserStubData;
import kosta.main.users.entity.User;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@MockBean(JpaMetamodelMappingContext.class)
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

    public List<CommunityPostListDTO> getCommunityPostListDTO(){
        CommunityPost communityPost = getCommunityPost();
        CommunityPost anotherCommunityPost = getAnotherCommunityPost();
        List<CommunityPostListDTO> communityPostLists = new ArrayList<>();
        communityPostLists.add(CommunityPostListDTO.from(communityPost));
        communityPostLists.add(CommunityPostListDTO.from(anotherCommunityPost));
        return communityPostLists;
    }


    public Page<CommunityPostListDTO> getCommunityPostListDTOPage(){
        List<CommunityPostListDTO> communityPostListDto = getCommunityPostListDTO();
        // 요청으로 들어온 page와 한 page당 원하는 데이터의 갯수
        PageRequest pageRequest = PageRequest.of(0, 10);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), communityPostListDto.size());
        return new PageImpl<>(communityPostListDto.subList(start, end), pageRequest, communityPostListDto.size());
    }

    public CommunityPostResponseDTO getCommunityPostResponseDTO(){
        CommunityPost communityPost = getCommunityPost();
        return CommunityPostResponseDTO.of(communityPost);
    }



    public CommunityPostUpdateDTO getCommunityPostUpdateDTO(){
        CommunityPost communityPost = getCommunityPost();
        return CommunityPostUpdateDTO.builder()
                .title(communityPost.getTitle())
                .content(communityPost.getContent())
                .build();
    }
    public CommunityPostDetailDTO getCommunityPostDetailDto(){
        CommunityPost communityPost = getCommunityPost();
        return CommunityPostDetailDTO.from(communityPost,true);
    }
    public CommunityPostCreateDTO getCommunityPostCreateDTO(){
        return CommunityPostCreateDTO.builder()
                .title(TITLE)
                .content(CONTENT)
                .build();
    }

    public CommunityPostDTO getCommunityPostDTO(){
        CommunityPost communityPost = getCommunityPost();
        return CommunityPostDTO.builder()
                .communityPostId(communityPost.getCommunityPostId())
                .title(communityPost.getTitle())
                .content(communityPost.getContent())
                .views(communityPost.getViews())
                .imagePaths(communityPost.getImages())
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
                "image/png",
                fileInputStream
        );

        return image1;
    }


}
