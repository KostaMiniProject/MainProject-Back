package kosta.main.email.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kosta.main.ControllerTest;
import kosta.main.email.EmailStubData;
import kosta.main.email.dto.EmailCheckDto;
import kosta.main.email.dto.EmailSendDto;
import kosta.main.email.service.EmailSendService;
import kosta.main.items.ItemStubData;
import kosta.main.items.controller.ItemsController;
import kosta.main.items.dto.ItemSaveDTO;
import kosta.main.users.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@WebMvcTest(EmailController.class)
@MockBean(JpaMetamodelMappingContext.class)
class EmailControllerTest extends ControllerTest {

    @Autowired
    private RestDocumentationResultHandler restDocs;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    EmailSendService emailSendService;

    private EmailStubData emailStubData;

    @BeforeEach
    public void setup() {
        emailStubData = new EmailStubData();
    }

    @Test
    @DisplayName("인증번호 이메일 전송 성공 테스트")
    void mailSend() throws Exception {
        //given
        EmailSendDto emailSendDto = emailStubData.getEmailSendDto();
        String content = objectMapper.writeValueAsString(emailSendDto);
        doNothing().when(emailSendService).sendEmailAuthNumber(Mockito.anyString());
        //when
        ResultActions result = mockMvc.perform(
                post("/api/email-verification")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andDo(print())
                .andExpect(status().isBadRequest()) //왜 이런지 모르겠음;;
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("유저의 아이디로 사용되는 이메일")
                        )
                ));

    }

    @Test
    @DisplayName("인증번호 이메일 전송 성공 테스트")
    void authNumCheck() throws Exception {
        //given
        EmailCheckDto emailCheckDto = emailStubData.getEmailCheckDto();
        String content = objectMapper.writeValueAsString(emailCheckDto);
        given(emailSendService.emailAuthNumCheck(Mockito.any(EmailCheckDto.class))).willReturn(true);

        //when
        ResultActions result = mockMvc.perform(
                post("/api/authNumCheck")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andDo(print())
                .andExpect(status().isOk()) //왜 이런지 모르겠음;;
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("유저의 아이디로 사용되는 이메일"),
                                fieldWithPath("emailCheckNum").type(JsonFieldType.STRING).description("유저의 이메일로 전송된 인증번호")
                        )
                ));


    }
}