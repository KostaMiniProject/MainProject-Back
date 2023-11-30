package kosta.main.users.service;

import kosta.main.users.UserStubData;
import kosta.main.users.dto.UserCreateDto;
import kosta.main.users.dto.UserCreateResponseDto;
import kosta.main.users.dto.UserUpdateDto;
import kosta.main.users.dto.UsersResponseDto;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    public static final int USER_ID = 1;
    @InjectMocks
    UsersService usersService;

    @Mock
    private UsersRepository usersRepository;



    private UserStubData userStubData;


    @BeforeEach
    void init(){
        userStubData = new UserStubData();
    }
    @Test
    void 내_정보_조회() {
        //given
        Integer userId = USER_ID;
        User user = userStubData.getUser();
        UsersResponseDto usersResponseDto = userStubData.getUsersResponseDto();
        given(usersRepository.findUserByUserId(Mockito.anyInt())).willReturn(Optional.of(usersResponseDto));
        //when
        UsersResponseDto result = usersService.findMyProfile(userId);
        //then

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
        UserCreateDto userCreateDto = userStubData.getUserCreateDto();

        given(usersRepository.save(Mockito.any(User.class))).willReturn(user);
        //when
        UserCreateResponseDto result = usersService.createUser(userCreateDto);

        //then
        assertThat(result.getName()).isEqualTo(user.getName());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getPhone()).isEqualTo(user.getPhone());
        assertThat(result.getAddress()).isEqualTo(user.getAddress());
    }

//    @Test
//    void 유저정보_수정() {
//        //given
//        Integer userId = USER_ID;
//        User updateUser = userStubData.getUpdateUser();
//        UserUpdateDto userUpdateDto = userStubData.getUserUpdateDto();
//
//        given(usersRepository.findById(Mockito.anyInt())).willReturn(Optional.of(new User()));
//        given(usersRepository.save(Mockito.any(User.class))).willReturn(updateUser);
//
//        //when
//        UsersResponseDto result = usersService.updateUser(userId, userUpdateDto, );
//        //then
//
//        assertThat(result.getName()).isEqualTo(updateUser.getName());
//        assertThat(result.getEmail()).isEqualTo(updateUser.getEmail());
//        assertThat(result.getAddress()).isEqualTo(updateUser.getAddress());
//        assertThat(result.getPhone()).isEqualTo(updateUser.getPhone());
//    }

    @Test
    void 유저_삭제() {
        //given
        Integer userId = USER_ID;
        User user = userStubData.getUser();

        given(usersRepository.findById(Mockito.anyInt())).willReturn(Optional.of(user));
        //when

        usersService.withdrawalUser(userId);

        //then
        assertThat(user.getUserStatus()).isEqualTo(User.UserStatus.DELETED);
    }
}