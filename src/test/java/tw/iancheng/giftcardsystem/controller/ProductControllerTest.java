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
import tw.iancheng.giftcardsystem.dto.ProductRequest;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 查詢商品列表
    @Test
    public void getProducts_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*]", hasSize(5)))
                .andExpect(jsonPath("$.size", equalTo(5)))
                .andExpect(jsonPath("$.totalPages", equalTo(1)))
                .andExpect(jsonPath("$.pageable.pageNumber", equalTo(0)));
    }

    @Test
    public void getProducts_filtering() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products")
                .param("search", "3")
                .param("categoryId", "2");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*]", hasSize(1)))
                .andExpect(jsonPath("$.size", equalTo(5)))
                .andExpect(jsonPath("$.totalPages", equalTo(1)))
                .andExpect(jsonPath("$.pageable.pageNumber", equalTo(0)));
    }

    @Test
    public void getProducts_sorting() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products")
                .param("sort", "price")
                .param("desc", "true");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*]", hasSize(5)))
                .andExpect(jsonPath("$.content[0].id", equalTo(5)))
                .andExpect(jsonPath("$.content[1].id", equalTo(4)))
                .andExpect(jsonPath("$.content[2].id", equalTo(3)))
                .andExpect(jsonPath("$.content[3].id", equalTo(2)))
                .andExpect(jsonPath("$.content[4].id", equalTo(1)))
                .andExpect(jsonPath("$.pageable.pageNumber", equalTo(0)))
                .andExpect(jsonPath("$.size", equalTo(5)))
                .andExpect(jsonPath("$.totalPages", equalTo(1)));
    }

    @Test
    public void getProducts_pagination() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products")
                .param("pageSize", "2")
                .param("pageNumber", "2");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*]", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", equalTo(1)))
                .andExpect(jsonPath("$.pageable.pageNumber", equalTo(2)))
                .andExpect(jsonPath("$.size", equalTo(2)))
                .andExpect(jsonPath("$.totalPages", equalTo(3)));
    }

    // 創建商品
    @Transactional
    @Test
    public void createProduct_success() throws Exception {
        ProductRequest productRequest = ProductRequest.builder()
                .name("TEST PRODUCT 6").description("TEST PRODUCT 6").price(new BigDecimal(60))
                .stockQuantity(new BigDecimal(60)).categoryId(1L)
                .build();

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.name", equalTo("TEST PRODUCT 6")))
                .andExpect(jsonPath("$.description", equalTo("TEST PRODUCT 6")))
                .andExpect(jsonPath("$.price", equalTo(60)))
                .andExpect(jsonPath("$.stockQuantity", equalTo(60)))
                .andExpect(jsonPath("$.category.id", equalTo(1)));
    }

    @Transactional
    @Test
    public void createProduct_illegalArgument() throws Exception {
        ProductRequest productRequest = ProductRequest.builder()
                .name("TEST PRODUCT 6")
                .build();

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }
}