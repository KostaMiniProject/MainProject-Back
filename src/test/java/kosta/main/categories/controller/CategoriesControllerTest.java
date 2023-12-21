package kosta.main.categories.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kosta.main.ControllerTest;
import kosta.main.bids.BidStubData;
import kosta.main.bids.controller.BidController;
import kosta.main.bids.dto.BidListResponseDTO;
import kosta.main.categories.CategoryStubData;
import kosta.main.categories.dto.CategoryResponseDTO;
import kosta.main.categories.service.CategoryService;
import kosta.main.global.annotation.WithMockCustomUser;
import kosta.main.users.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Description;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({SpringExtension.class})
@WebMvcTest(CategoriesController.class)
@MockBean(JpaMetamodelMappingContext.class)
class CategoriesControllerTest extends ControllerTest {


    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestDocumentationResultHandler restDocs;

    @MockBean
    private CategoryService categoryService;

    private final String BASE_URL = "/api/category";
    private CategoryStubData categoryStubData;

    @BeforeEach
    public void setup() {
        categoryStubData = new CategoryStubData();
    }

    @Test
    @Description("카테고리를 전부 가져오는 기능 테스트")
    void getAllCategory() throws Exception {
        //given
        List<CategoryResponseDTO> categoryResponseDTOs = categoryStubData.getCategoryResponseDTOs();;
        given(categoryService.findAllCategory()).willReturn(categoryResponseDTOs);

        //when

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL));

        //then

        result.andDo(print())
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                       responseFields(
                                fieldWithPath("[].categoryId").type(JsonFieldType.NUMBER).description("카테고리 ID값"),
                                fieldWithPath("[].categoryName").type(JsonFieldType.STRING).description("현재 카테고리의 이름")
                      )
                ));

    }
}