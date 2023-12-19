package kosta.main.users;

import kosta.main.users.dto.request.UserCreateDTO;
import kosta.main.users.dto.response.UserCreateResponseDTO;
import kosta.main.users.dto.request.UserUpdateDTO;
import kosta.main.users.dto.response.UsersResponseDTO;
import kosta.main.users.entity.User;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

public class UserStubData {


    public static final int USER_ID = 1;
    public static final String NAME = "테스트이름";
    public static final String PHONE = "010-1234-5678";
    public static final String PASSWORD = "asdfQWER1234!";
    public static final String ADDRESS = "경기도 성남시 분당구 성남대로 지하55";
    public static final String EMAIL = "hongildong@gmail.com";
    public static final String PROFILE_IMAGE = "프로필 이미지";
    public static final User.UserStatus USER_STATUS = User.UserStatus.ACTIVATE;
    public static final int USER_ID2 = 2;
    public static final String UPDATED_NAME = "수정이름";
    public static final String UPDATED_PHONE = "010-8765-4321";
    public static final String UPDATE_PASSWORD = "updatePassword1234!";
    public static final String UPDATE_ADDRESS = "업데이트주소명";
    public static final String UPDATE_EMAIL = "seongsookim@naver.com";
    public static final String UPDATE_IMAGE_FILE = "수정이미지파일";

    public User getUser(){
        return User.builder()
                .userId(USER_ID)
                .name(NAME)
                .phone(PHONE)
                .password(PASSWORD)
                .address(ADDRESS)
                .email(EMAIL)
                .profileImage(PROFILE_IMAGE)
                .userStatus(USER_STATUS)
                .build();
    }

    public User getUpdateUser(){
        return User.builder()
                .userId(USER_ID)
                .name(UPDATED_NAME)
                .phone(UPDATED_PHONE)
                .password(UPDATE_PASSWORD)
                .address(UPDATE_ADDRESS)
                .email(UPDATE_EMAIL)
                .profileImage(UPDATE_IMAGE_FILE)
                .userStatus(USER_STATUS)
                .build();

    }
    public UsersResponseDTO getUsersResponseDto(){
        return UsersResponseDTO.of(getUser());
    }

    public UserCreateDTO getUserCreateDto(){

        return UserCreateDTO.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .checkPassword(PASSWORD)
                .name(NAME)
                .address(ADDRESS)
                .phone(PHONE)
                .build();
    }

    public UserCreateResponseDTO getUserCreateResponseDTO(){
        return new UserCreateResponseDTO
                (EMAIL, NAME, ADDRESS, PHONE);
    }

    public UserUpdateDTO getUserUpdateDTO() {
        return new UserUpdateDTO(
                UPDATE_PASSWORD,
                UPDATE_PASSWORD,
                UPDATED_NAME,
                UPDATE_ADDRESS,
                UPDATED_PHONE,
                UPDATE_IMAGE_FILE,
                USER_STATUS
        );
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
    public MultipartFile getMultipartFile() throws IOException {
        final String fileName = "testImage1"; //파일명
        final String contentType = "png"; //파일타입
        final String filePath = "src/test/resources/testImage/"+fileName+"."+contentType; //파일경로
        FileInputStream fileInputStream = new FileInputStream(filePath);

        //파일생성
        MultipartFile image1 = new MockMultipartFile(
                "file", //name
                fileName + "." + contentType, //originalFilename
                "image/png",
                fileInputStream
        );

        return image1;
    }
}
