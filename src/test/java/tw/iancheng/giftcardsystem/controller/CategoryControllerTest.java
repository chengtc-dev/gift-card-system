package tw.iancheng.giftcardsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tw.iancheng.giftcardsystem.dto.category.CategoryCreateRequest;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 查詢目錄列表
    @Test
    public void getCategories_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/categories");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]", hasSize(3)))
                .andExpect(jsonPath("$.[*].name", notNullValue()));
    }

    // 創建目錄
    @Transactional
    @Test
    public void createCategory_success() throws Exception {
        CategoryCreateRequest categoryCreateRequest = CategoryCreateRequest.builder()
                .name("TEST CATEGORY 4")
                .build();

        String json = objectMapper.writeValueAsString(categoryCreateRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.name", equalTo("TEST CATEGORY 4")));
    }

    @Transactional
    @Test
    public void createCategory_illegalArgument() throws Exception {
        CategoryCreateRequest categoryCreateRequest = CategoryCreateRequest.builder()
                .build();

        String json = objectMapper.writeValueAsString(categoryCreateRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }
}