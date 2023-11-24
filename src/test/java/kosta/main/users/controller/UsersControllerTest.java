package kosta.main.users.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kosta.main.users.UserStubData;
import kosta.main.users.dto.UserCreateDto;
import kosta.main.users.dto.UserCreateResponseDto;
import kosta.main.users.dto.UserUpdateDto;
import kosta.main.users.dto.UsersResponseDto;
import kosta.main.users.entity.User;
import kosta.main.users.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UsersController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class UsersControllerTest {

    public static final String FIND_MY_PROFILE_URL = "/users/{userId}";
    public static final String SIGNUP_URL = "/signup";
    public static final String DELETE_USER_URL = "/users/{userId}";
    @Autowired
    private MockMvc mockMvc;

    private UserStubData userStubData;
    private ObjectMapper objectMapper;

    @MockBean
    private UsersService usersService;

    @BeforeEach
    void init(){
        userStubData = new UserStubData();
        objectMapper = new ObjectMapper();
    }

    @Test
    void 유저_프로필_조회() throws Exception {
        User user = userStubData.getUser();
        //given
        given(usersService.findMyProfile(Mockito.anyInt())).willReturn(userStubData.getUsersResponseDto());

        //when
        ResultActions action = mockMvc.perform(
                get(FIND_MY_PROFILE_URL, 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        action.andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.address").value(user.getAddress()))
                .andExpect(jsonPath("$.phone").value(user.getPhone()))
                .andExpect(jsonPath("$.profileImage").value(user.getProfileImage()));
    }

    @Test
    void 회원가입() throws Exception {
        //given
        UserCreateDto userCreateDto = userStubData.getUserCreateDto();
        UserCreateResponseDto userCreateResponseDto = userStubData.getUserCreateResponseDto();
        String content = objectMapper.writeValueAsString(userCreateDto);

        given(usersService.createUser(Mockito.any(UserCreateDto.class))).willReturn(userCreateResponseDto);
        //when

        ResultActions result = mockMvc.perform(
                post(SIGNUP_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(userCreateDto.getEmail()))
                .andExpect(jsonPath("$.name").value(userCreateDto.getName()))
                .andExpect(jsonPath("$.address").value(userCreateDto.getAddress()))
                .andExpect(jsonPath("$.phone").value(userCreateDto.getPhone()));

    }

    @Test
    void 내_정보_수정() throws Exception {
        User user = userStubData.getUser();
        UsersResponseDto usersResponseDto = userStubData.getUsersResponseDto();
        String content = objectMapper.writeValueAsString(usersResponseDto);

        given(usersService.updateUser(Mockito.anyInt(), Mockito.any(UserUpdateDto.class))).willReturn(usersResponseDto);

        ResultActions result = mockMvc.perform(
                put("/users/{userId}", 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)

        );
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.address").value(user.getAddress()))
                .andExpect(jsonPath("$.phone").value(user.getPhone()))
                .andExpect(jsonPath("$.profileImage").value(user.getProfileImage()));
    }

    //고민좀 해보고 작성하겠습니다.
//    @Test
//    void 탈퇴하기() throws Exception {
//        //given
//        given(usersService.withdrawalUser(Mockito.anyInt())).willReturn()
//        //when
//        ResultActions result = mockMvc.perform(
//                put(DELETE_USER_URL, 1)
//        );
//
//        //then
//        verify(usersService,times(1)).withdrawalUser(Mockito.anyInt());
//
//        result.andExpect(status().isNoContent());
//    }
}