package kosta.main.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kosta.main.global.annotation.WithMockCustomUser;
import kosta.main.items.entity.Item;
import kosta.main.users.UserStubData;
import kosta.main.users.dto.UserCreateDto;
import kosta.main.users.dto.UserCreateResponseDto;
import kosta.main.users.dto.UserUpdateDto;
import kosta.main.users.dto.UsersResponseDto;
import kosta.main.users.entity.User;
import kosta.main.users.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(UsersController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(kosta.main.RestDocsConfiguration.class)
class UsersControllerTest {

    public static final String BASIC_URL = "/api/users";
    public static final String SIGNUP_URL = "/api/signup";
    public static final int ONE_ACTION = 1;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestDocumentationResultHandler restDocs;

    @MockBean
    private UsersService usersService;
    private UserStubData userStubData;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
        userStubData = new UserStubData();
        MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .alwaysDo(print())														// 이건 왜하는지 모르겠음.
                .alwaysDo(restDocs)														// 재정의한 핸들러를 적용함. 적용하면 일반 document에도 적용됨. 일반 document로 선언되면 그부분도 같이 생성됨에 유의해야 함.
                .addFilters(new CharacterEncodingFilter("UTF-8", true))					// 한글깨짐 방지 처리
                .build();
    }

    @Test
    @WithMockCustomUser
    @DisplayName("유저 프로필 조회")
    void findMyProfile() throws Exception {
        User user = userStubData.getUser();
        //given
        given(usersService.findMyProfile(Mockito.any(User.class))).willReturn(userStubData.getUsersResponseDto());

        //when
        ResultActions action = mockMvc.perform(
                get(BASIC_URL, 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        action
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.address").value(user.getAddress()))
                .andExpect(jsonPath("$.phone").value(user.getPhone()))
                .andExpect(jsonPath("$.profileImage").value(user.getProfileImage()));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("회원가입")
    void signup() throws Exception {
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
                        .with(csrf())
        );

        //then
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(userCreateDto.getEmail()))
                .andExpect(jsonPath("$.name").value(userCreateDto.getName()))
                .andExpect(jsonPath("$.address").value(userCreateDto.getAddress()))
                .andExpect(jsonPath("$.phone").value(userCreateDto.getPhone()));

    }

    @Test
    @WithMockCustomUser
    @DisplayName("내 정보 수정")
    void updateMyInfo() throws Exception {
        User user = userStubData.getUser();
        UserUpdateDto userUpdateDto = userStubData.getUserUpdateDto();
        String content = objectMapper.writeValueAsString(userUpdateDto);

        UsersResponseDto usersResponseDto = userStubData.getUsersResponseDto();
        MockMultipartFile file = userStubData.getMockMultipartFile();

        MockPart userUpdateDto1 = new MockPart("userUpdateDto", content.getBytes(StandardCharsets.UTF_8));
        userUpdateDto1.getHeaders().setContentType(APPLICATION_JSON);

        given(usersService.updateUser(
                Mockito.any(User.class),
                Mockito.any(UserUpdateDto.class),
                Mockito.any(MultipartFile.class)))
                .willReturn(usersResponseDto);

        //Then
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.multipart(HttpMethod.PUT,BASIC_URL)
                        .file(file)
                        .part(userUpdateDto1)
                        .with(csrf()));


        perform
                .andDo(print())
                .andExpect(status().isOk())
//                .andDo(restDocs.document());
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.address").value(user.getAddress()))
                .andExpect(jsonPath("$.phone").value(user.getPhone()))
                .andExpect(jsonPath("$.profileImage").value(user.getProfileImage()));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("회원탈퇴")
    void 탈퇴하기() throws Exception {
        //given
        doNothing().when(usersService).withdrawalUser(Mockito.any());
        //when
        ResultActions result = mockMvc.perform(
                put(BASIC_URL+"/withdrawal")
                        .with(csrf())
        );

        //then
        verify(usersService,times(ONE_ACTION)).withdrawalUser(Mockito.any(User.class));

        result.andDo(print())
                .andExpect(status().isNoContent());
    }
}