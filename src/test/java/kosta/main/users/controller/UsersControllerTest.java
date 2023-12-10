package kosta.main.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kosta.main.ControllerTest;
import kosta.main.global.annotation.WithMockCustomUser;
import kosta.main.users.UserStubData;
import kosta.main.users.dto.UserCreateDTO;
import kosta.main.users.dto.UserCreateResponseDTO;
import kosta.main.users.dto.UserUpdateDTO;
import kosta.main.users.dto.UsersResponseDTO;
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
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@WebMvcTest(UsersController.class)
@MockBean(JpaMetamodelMappingContext.class)
class UsersControllerTest extends ControllerTest {

    public static final String BASIC_URL = "/api/users";
    public static final String SIGNUP_URL = "/api/signup";
    public static final int ONE_ACTION = 1;


    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestDocumentationResultHandler restDocs;

    @MockBean
    private UsersService usersService;
    private UserStubData userStubData;

    @BeforeEach
    public void setup() {
        userStubData = new UserStubData();
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
                        .header("Authorization", "Bearer yourAccessToken")
        );
        //then
        action
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("유저의 아이디로 사용되는 이메일"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("유저의 이름"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("유저의 주소지"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("유저의 전화번호"),
                                fieldWithPath("profileImage").type(JsonFieldType.STRING).description("유저의 프로필 이미지"),
                                fieldWithPath("userStatus").type(JsonFieldType.STRING).description("유저의 상태(ACTIVATE, BANNED ,DELETED)")
                                )
                ))
                .andReturn();
    }

    @Test
    @WithMockCustomUser
    @DisplayName("회원가입")
    void signup() throws Exception {
        //given
        UserCreateDTO userCreateDto = userStubData.getUserCreateDto();
        UserCreateResponseDTO userCreateResponseDto = userStubData.getUserCreateResponseDTO();
        String content = objectMapper.writeValueAsString(userCreateDto);

        given(usersService.createUser(Mockito.any(UserCreateDTO.class))).willReturn(userCreateResponseDto);
        //when

        ResultActions result = mockMvc.perform(
                post(SIGNUP_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        //then
        result.andDo(print())
                .andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("유저의 아이디로 사용되는 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("유저의 비밀번호"),
                                fieldWithPath("checkPassword").type(JsonFieldType.STRING).description("유저의 확인 비밀번호"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("유저의 이름"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("유저의 주소지"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("유저의 전화번호")
                        ),
                        responseFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("유저의 아이디로 사용되는 이메일"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("유저의 이름"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("유저의 주소지"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("유저의 전화번호")
                        )
                ));

    }

    @Test
    @WithMockCustomUser
    @DisplayName("내 정보 수정")
    void updateMyInfo() throws Exception {
        User user = userStubData.getUser();
        UserUpdateDTO userUpdateDto = userStubData.getUserUpdateDTO();
        String content = objectMapper.writeValueAsString(userUpdateDto);

        UsersResponseDTO usersResponseDto = userStubData.getUsersResponseDto();
        MockMultipartFile file = userStubData.getMockMultipartFile();

        MockPart userUpdateDto1 = new MockPart("userUpdateDto",content.getBytes(StandardCharsets.UTF_8));
        userUpdateDto1.getHeaders().setContentType(APPLICATION_JSON);

        given(usersService.updateUser(
                Mockito.any(User.class),
                Mockito.any(UserUpdateDTO.class),
                Mockito.any(MultipartFile.class)))
                .willReturn(usersResponseDto);

        //Then
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.multipart(HttpMethod.PUT,BASIC_URL)
                        .file(file)
                        .part(userUpdateDto1)
                        .header("Authorization", "Bearer yourAccessToken")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        );


        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        requestParts(
                                partWithName("userUpdateDto").description("유저 업데이트 정보"),
                                partWithName("file").description("유저 프로필 사진")
                        ),
                        requestPartFields("userUpdateDto",
                                fieldWithPath("password").type(JsonFieldType.STRING).description("유저의 변경용 비밀번호"),
                                fieldWithPath("checkPassword").type(JsonFieldType.STRING).description("유저의 변경용 비밀번호 확인"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("유저의 이름"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("유저의 주소지"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("유저의 전화번호"),
                                fieldWithPath("profileImage").type(JsonFieldType.STRING).description("유저의 프로필 이미지"),
                                fieldWithPath("userStatus").type(JsonFieldType.STRING).description("유저의 상태(ACTIVATE, BANNED ,DELETED)")
                        )
                        ,responseFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("유저의 아이디로 사용되는 이메일"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("유저의 이름"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("유저의 주소지"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("유저의 전화번호"),
                                fieldWithPath("profileImage").type(JsonFieldType.STRING).description("유저의 프로필 이미지"),
                                fieldWithPath("userStatus").type(JsonFieldType.STRING).description("유저의 상태(ACTIVATE, BANNED ,DELETED)")
                        )
                ));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("회원탈퇴")
    void withdrawal() throws Exception {
        //given
        doNothing().when(usersService).withdrawalUser(Mockito.any());
        //when
        ResultActions result = mockMvc.perform(
                delete(BASIC_URL+"/withdrawal")
                        .header("Authorization", "Bearer yourAccessToken")
        );

        //then
        verify(usersService,times(ONE_ACTION)).withdrawalUser(Mockito.any(User.class));

        result.andDo(print())
                .andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        )
                ));
    }
}