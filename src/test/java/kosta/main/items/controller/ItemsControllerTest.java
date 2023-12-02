package kosta.main.items.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kosta.main.communityposts.CommunityPostStubData;
import kosta.main.communityposts.controller.CommunityPostsController;
import kosta.main.global.annotation.WithMockCustomUser;
import kosta.main.items.ItemStubData;
import kosta.main.items.service.ItemsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(ItemsController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(kosta.main.RestDocsConfiguration.class)
class ItemsControllerTest {

    @Autowired
    private RestDocumentationResultHandler restDocs;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private ItemStubData itemStubData;

    @MockBean
    ItemsService itemsService;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
        itemStubData = new ItemStubData();
        MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .alwaysDo(print())
                .alwaysDo(restDocs)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }
    private final String BASE_URL = "/items";
    @Test
    @WithMockCustomUser
    @DisplayName("물건 추가 성공 테스트")
    void addItem() {
    }

    @Test
    void getItems() {
    }

    @Test
    void getFindById() {
    }

    @Test
    void updateItem() {
    }

    @Test
    void deleteItem() {
    }
}