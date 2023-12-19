package kosta.main.users.service;

import kosta.main.blockedusers.repository.BlockedUsersRepository;
import kosta.main.global.s3upload.service.ImageService;
import kosta.main.reports.repository.ReportsRepository;
import kosta.main.users.UserStubData;
import kosta.main.users.dto.request.UserCreateDTO;
import kosta.main.users.dto.response.UserCreateResponseDTO;
import kosta.main.users.dto.request.UserUpdateDTO;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    public static final int USER_ID = 1;
    public static final String CHANGED_PASSWORD = "변경된 비밀번호";
    public static final String IMAGE_PATH = "이미지 경로";
    @InjectMocks
    UsersService usersService;


    @Mock
    private UsersRepository usersRepository;
    @Mock
    private ReportsRepository reportsRepository;
    @Mock
    private BlockedUsersRepository blockedUsersRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private PasswordEncoder passwordEncoder;



    private UserStubData userStubData;


    @BeforeEach
    void init(){
        userStubData = new UserStubData();
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


}