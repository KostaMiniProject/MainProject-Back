package kosta.main.users;

import kosta.main.blockedusers.entity.BlockedUser;
import kosta.main.users.dto.request.*;
import kosta.main.users.dto.response.UserCreateResponseDTO;
import kosta.main.users.dto.response.UsersResponseDTO;
import kosta.main.users.entity.User;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

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
    public static final String ADDRESS_DETAIL = "주소 상세";
    public static final String JIBUN_ADDR = "지번주소";
    public static final String ROAD_ADDR = "도로명주소";
    public static final String ZCODE = "우편번호";
    public static final int ANOTHER_USER_ID = 2;
    public static final String ANOTHER_USER_NAME = "홍길서";
    public static final String ANOTHER_PHONE = "010-1234-1111";
    public static final int BLOCKED_USER_ID = 1;

    public User getUser(){
        User build = User.builder()
                .userId(USER_ID)
                .name(NAME)
                .phone(PHONE)
                .password(PASSWORD)
                .address(ADDRESS)
                .email(EMAIL)
                .profileImage(PROFILE_IMAGE)
                .userStatus(USER_STATUS)
                .build();
        return build;
    }
    public User getHaveBlockUser(){
        User build = User.builder()
                .userId(USER_ID)
                .name(NAME)
                .phone(PHONE)
                .password(PASSWORD)
                .address(ADDRESS)
                .email(EMAIL)
                .profileImage(PROFILE_IMAGE)
                .userStatus(USER_STATUS)
                .build();
        User anotherUser = getAnotherUser();
        BlockedUser blockedUser = BlockedUser.builder()
                .blockedUserId(BLOCKED_USER_ID)
                .user(build)
                .blockingUser(anotherUser)
                .build();
        build.addBlockedUser(blockedUser);
        return build;
    }

    public User getUpdateUser(){
        User build = User.builder()
                .userId(USER_ID)
                .name(UPDATED_NAME)
                .phone(UPDATED_PHONE)
                .password(UPDATE_PASSWORD)
                .address(UPDATE_ADDRESS)
                .email(UPDATE_EMAIL)
                .profileImage(UPDATE_IMAGE_FILE)
                .userStatus(USER_STATUS)
                .build();
        return build;
    }
    public User getAnotherUser(){
        return User.builder()
                .userId(ANOTHER_USER_ID)
                .name(ANOTHER_USER_NAME)
                .phone(ANOTHER_PHONE)
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
                .passwordConfirm(PASSWORD)
                .nickName(NAME)
                .address(getAddressDTO())
                .addressDetail(ADDRESS_DETAIL)
                .phone(PHONE)
                .build();
    }
    public AddressDTO getAddressDTO(){
        return new AddressDTO(JIBUN_ADDR, ROAD_ADDR, ZCODE);
    }

    public UserCreateResponseDTO getUserCreateResponseDTO(){
        return new UserCreateResponseDTO
                (EMAIL, NAME, ADDRESS, PHONE);
    }
    public UserFindIdDTO getUserFindIdDTO(){
        return new UserFindIdDTO(NAME, PHONE);
    }

    public UserUpdateDTO getUserUpdateDTO() {
        return new UserUpdateDTO(
                UPDATE_PASSWORD,
                UPDATE_PASSWORD,
                UPDATED_NAME,
                getAddressDTO(),
                ADDRESS_DETAIL,
                UPDATED_PHONE,
                UPDATE_IMAGE_FILE,
                USER_STATUS
        );
    }
    public UserFindPasswordDTO getUserFindPasswordDTO(){
        return UserFindPasswordDTO.from(EMAIL, NAME, PHONE);
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
