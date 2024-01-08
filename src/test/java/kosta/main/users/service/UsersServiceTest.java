package kosta.main.users.service;

import kosta.main.bids.repository.BidRepository;
import kosta.main.blockedusers.dto.BlockedUserResponseDTO;
import kosta.main.blockedusers.repository.BlockedUsersRepository;
import kosta.main.communityposts.CommunityPostStubData;
import kosta.main.communityposts.dto.CommunityPostListDTO;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.communityposts.repository.CommunityPostsRepository;
import kosta.main.email.service.EmailSendService;
import kosta.main.exchangeposts.ExchangePostStubData;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.exchangeposts.repository.ExchangePostsRepository;
import kosta.main.global.s3upload.service.ImageService;
import kosta.main.reports.repository.ReportsRepository;
import kosta.main.users.UserStubData;
import kosta.main.users.dto.request.UserCreateDTO;
import kosta.main.users.dto.request.UserFindIdDTO;
import kosta.main.users.dto.request.UserFindPasswordDTO;
import kosta.main.users.dto.response.UserCreateResponseDTO;
import kosta.main.users.dto.request.UserUpdateDTO;
import kosta.main.users.dto.response.UserExchangePostResponseDTO;
import kosta.main.users.dto.response.UsersResponseDTO;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;


@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    public static final int USER_ID = 1;
    public static final String CHANGED_PASSWORD = "변경된 비밀번호";
    public static final String IMAGE_PATH = "이미지 경로";
    public static final int BID_COUNT = 1;
    public static final String AUTH_NUMBER = "abcdef";
    public static final int ANOTHER_USER_ID = 2;
    @InjectMocks
    UsersService usersService;


    @Mock
    private UsersRepository usersRepository;
    @Mock
    private ReportsRepository reportsRepository;
    @Mock
    private BlockedUsersRepository blockedUsersRepository;
    @Mock
    private CommunityPostsRepository communityPostsRepository;
    @Mock
    private BidRepository bidRepository;

    @Mock
    private ExchangePostsRepository exchangePostsRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private EmailSendService emailSendService;
    @Mock
    private PasswordEncoder passwordEncoder;



    private UserStubData userStubData;
    private CommunityPostStubData communityPostStubData;
    private ExchangePostStubData exchangePostStubData;


    @BeforeEach
    void init(){
        userStubData = new UserStubData();
        communityPostStubData = new CommunityPostStubData();
        exchangePostStubData = new ExchangePostStubData();
    }
    @Test
    void findMyProfile() {
        //given
        User user = userStubData.getUser();
        //when
        UsersResponseDTO result = usersService.findMyProfile(user);
//        then

        assertThat(result.getName()).isEqualTo(user.getName());
        assertThat(result.getPhone()).isEqualTo(user.getPhone());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getPhone()).isEqualTo(user.getPhone());
        assertThat(result.getAddress()).isEqualTo(user.getAddress());
    }

    @Test
    void 유저_생성() {
        //given
        User user = userStubData.getUser();
        UserCreateDTO userCreateDto = userStubData.getUserCreateDto();
        given(passwordEncoder.encode(Mockito.anyString())).willReturn(CHANGED_PASSWORD);
        given(usersRepository.save(Mockito.any(User.class))).willReturn(user);
        //when
        UserCreateResponseDTO result = usersService.createUser(userCreateDto);

        //then
        assertThat(result.getName()).isEqualTo(user.getName());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getPhone()).isEqualTo(user.getPhone());
        assertThat(result.getAddress()).isEqualTo(user.getAddress());
    }

    @Test
    void 유저정보_수정() throws IOException {
        //given
        User user = userStubData.getUser();
        User updateUser = userStubData.getUpdateUser();
        UserUpdateDTO userUpdateDto = userStubData.getUserUpdateDTO();
        MultipartFile multipartFile = userStubData.getMultipartFile();

        given(imageService.resizeToProfileSizeAndUpload(Mockito.any(MultipartFile.class))).willReturn(IMAGE_PATH);

        given(passwordEncoder.encode(Mockito.anyString())).willReturn(CHANGED_PASSWORD);
        given(usersRepository.save(Mockito.any(User.class))).willReturn(updateUser);

        //when
        UsersResponseDTO result = usersService.updateUser(user, userUpdateDto, multipartFile);
        //then

        assertThat(result.getName()).isEqualTo(updateUser.getName());
        assertThat(result.getEmail()).isEqualTo(updateUser.getEmail());
        assertThat(result.getAddress()).isEqualTo(updateUser.getAddress());
        assertThat(result.getPhone()).isEqualTo(updateUser.getPhone());
    }


    @Test
    void blockUser(){
        //given
        User user = userStubData.getUser();
        User anotherUser = userStubData.getAnotherUser();
        given(usersRepository.findById(Mockito.anyInt())).willReturn(Optional.of(anotherUser));
        given(usersRepository.findById(Mockito.anyInt())).willReturn(Optional.of(user));
        //when
        boolean b = usersService.blockUser(ANOTHER_USER_ID, user);
        //then
        assertThat(b).isTrue();
    }

    @Test
    void getBlockedUsers(){
        //given
        User user = userStubData.getHaveBlockUser();
        User anotherUser = userStubData.getAnotherUser();
        given(usersRepository.findById(Mockito.anyInt())).willReturn(Optional.of(user));
        //when
        List<BlockedUserResponseDTO> blockedUsers = usersService.getBlockedUsers(user);
        //then
        assertThat(blockedUsers.size()).isEqualTo(1);
    }

    @Test
    void findIdByNamePhone(){
        UserFindIdDTO userFindIdDTO = userStubData.getUserFindIdDTO();
        UsersResponseDTO usersResponseDto = userStubData.getUsersResponseDto();
        //given
        given(usersRepository.findUserByUserName(Mockito.anyString())).willReturn(Optional.of(usersResponseDto));
        //when
        String idByNamePhone = usersService.findIdByNamePhone(userFindIdDTO);
        //then
        assertThat(idByNamePhone).isEqualTo(usersResponseDto.getEmail());

    }

    @Test
    void findIdByNamePhoneEmail(){
        UserFindPasswordDTO userFindPasswordDTO = userStubData.getUserFindPasswordDTO();
        UsersResponseDTO usersResponseDto = userStubData.getUsersResponseDto();
        //given
        given(usersRepository.findUserByUserName(Mockito.anyString())).willReturn(Optional.of(usersResponseDto));
        given(emailSendService.sendEmailNewPassword(Mockito.anyString())).willReturn(AUTH_NUMBER);
        //when
        String idByNamePhoneEmail = usersService.findIdByNamePhoneEmail(userFindPasswordDTO);
        //then
        assertThat(idByNamePhoneEmail).isEqualTo(usersResponseDto.getEmail());
    }
    @Test
    void findMyExchangePostList(){
        //given
        User user = userStubData.getUser();
        Page<ExchangePost> exchangePostPages = exchangePostStubData.getExchangePostPages();
        given(exchangePostsRepository.findByUser_UserId(Mockito.any(Pageable.class), Mockito.anyInt())).willReturn(exchangePostPages);
        given(bidRepository.countByExchangePostAndStatusNotDeleted(Mockito.any(ExchangePost.class))).willReturn(BID_COUNT);
        //when
        Page<UserExchangePostResponseDTO> myExchangePostList = usersService.findMyExchangePostList(exchangePostPages.getPageable(), user);
        //then
        assertThat(myExchangePostList.getSize()).isEqualTo(exchangePostPages.getSize());
    }

    @Test
    void findMyCommunityPostList(){
        //given
        User user = userStubData.getUser();
        Page<CommunityPost> communityPostPage = communityPostStubData.getCommunityPostPage();
        given(communityPostsRepository.findByUser_UserId(Mockito.any(Pageable.class), Mockito.anyInt())).willReturn(communityPostPage);

        //when
        Page<CommunityPostListDTO> myCommunityPostList = usersService.findMyCommunityPostList(communityPostPage.getPageable(), user);
        //then

        assertThat(myCommunityPostList.getSize()).isEqualTo(communityPostPage.getSize());
    }

}