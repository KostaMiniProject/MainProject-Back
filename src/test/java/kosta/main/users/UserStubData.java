package kosta.main.users;

import kosta.main.users.dto.UserCreateDto;
import kosta.main.users.dto.UserCreateResponseDto;
import kosta.main.users.dto.UserUpdateDto;
import kosta.main.users.dto.UsersResponseDto;
import kosta.main.users.entity.User;

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
                //.profileImage(PROFILE_IMAGE)
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
                //.profileImage(UPDATE_IMAGE_FILE)
                .userStatus(USER_STATUS)
                .build();

    }
    public UsersResponseDto getUsersResponseDto(){
        return UsersResponseDto.of(getUser());
    }

    public UserCreateDto getUserCreateDto(){

        return UserCreateDto.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .name(NAME)
                .address(ADDRESS)
                .phone(PHONE)
                .profileImage(PROFILE_IMAGE)
                .build();
    }

    public UserCreateResponseDto getUserCreateResponseDto(){
        return new UserCreateResponseDto
                (EMAIL, NAME, ADDRESS, PHONE);
    }

    public UserUpdateDto getUserUpdateDto() {
        return new UserUpdateDto(
                UPDATE_PASSWORD,
                UPDATED_NAME,
                UPDATE_ADDRESS,
                UPDATED_PHONE,
                UPDATE_IMAGE_FILE,
                USER_STATUS
        );
    }
}
