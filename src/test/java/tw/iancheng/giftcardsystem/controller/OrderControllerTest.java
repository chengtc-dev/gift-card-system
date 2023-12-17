package tw.iancheng.giftcardsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import tw.iancheng.giftcardsystem.dto.order.BuyItem;
import tw.iancheng.giftcardsystem.dto.order.OrderCreateRequest;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 查詢訂單列表
    @Test
    public void getOrders() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{userId}/orders", 1);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*]", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", equalTo(1)))
                .andExpect(jsonPath("$.content[0].totalAmount", equalTo(140.00)))
                .andExpect(jsonPath("$.content[0].userId", equalTo(1)))
                .andExpect(jsonPath("$.content[0].orderItems", hasSize(3)))
                .andExpect(jsonPath("$.pageable.pageNumber", equalTo(0)))
                .andExpect(jsonPath("$.size", equalTo(5)))
                .andExpect(jsonPath("$.totalPages", equalTo(1)));
    }

    @Test
    public void getOrders_pagination() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{userId}/orders", 1)
                .param("pageSize", "2")
                .param("pageNumber", "2");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageable.pageNumber", equalTo(2)))
                .andExpect(jsonPath("$.size", equalTo(2)))
                .andExpect(jsonPath("$.totalPages", equalTo(1)))
                .andExpect(jsonPath("$.content[*]", hasSize(0)));
    }

    @Test
    public void getOrders_userHasNoOrder() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{userId}/orders", 2);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageable.pageNumber", notNullValue()))
                .andExpect(jsonPath("$.size", notNullValue()))
                .andExpect(jsonPath("$.totalPages", notNullValue()))
                .andExpect(jsonPath("$.content[*]", hasSize(0)));
    }

    @Test
    public void getOrders_userNotExist() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/{userId}/orders", 100);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageable.pageNumber", notNullValue()))
                .andExpect(jsonPath("$.size", notNullValue()))
                .andExpect(jsonPath("$.totalPages", notNullValue()))
                .andExpect(jsonPath("$.content[*]", hasSize(0)));
    }

    // 創建訂單
    @Transactional
    @Test
    public void createOrder_success() throws Exception {
        List<BuyItem> buyItemList = new ArrayList<>();

        BuyItem buyItem1 = BuyItem.builder()
                .productId(1L).quantity(1)
                .build();
        buyItemList.add(buyItem1);

        BuyItem buyItem2 = BuyItem.builder()
                .productId(2L).quantity(2)
                .build();
        buyItemList.add(buyItem2);

        OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
                .buyItems(buyItemList)
                .build();

        String json = objectMapper.writeValueAsString(orderCreateRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/{userId}/orders", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.userId", equalTo(1)))
                .andExpect(jsonPath("$.totalAmount", equalTo(50.0)))
                .andExpect(jsonPath("$.orderItems", hasSize(2)));
    }

    @Transactional
    @Test
    public void createOrder_illegalArgument_emptyBuyItemList() throws Exception {
        List<BuyItem> buyItemList = new ArrayList<>();

        OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
                .buyItems(buyItemList)
                .build();

        String json = objectMapper.writeValueAsString(orderCreateRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/{userId}/orders", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Transactional
    @Test
    public void createOrder_userNotExist() throws Exception {
        List<BuyItem> buyItemList = new ArrayList<>();

        BuyItem buyItem1 = BuyItem.builder()
                .productId(1L).quantity(1)
                .build();
        buyItemList.add(buyItem1);

        OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
                .buyItems(buyItemList)
                .build();

        String json = objectMapper.writeValueAsString(orderCreateRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/{userId}/orders", 100)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Transactional
    @Test
    public void createOrder_productNotExist() throws Exception {
        List<BuyItem> buyItemList = new ArrayList<>();

        BuyItem buyItem1 = BuyItem.builder()
                .productId(100L).quantity(1)
                .build();
        buyItemList.add(buyItem1);

        OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
                .buyItems(buyItemList)
                .build();

        String json = objectMapper.writeValueAsString(orderCreateRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/{userId}/orders", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Transactional
    @Test
    public void createOrder_stockNotEnough() throws Exception {
        List<BuyItem> buyItemList = new ArrayList<>();

        BuyItem buyItem1 = BuyItem.builder()
                .productId(1L).quantity(100)
                .build();
        buyItemList.add(buyItem1);

        OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
                .buyItems(buyItemList)
                .build();

        String json = objectMapper.writeValueAsString(orderCreateRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/{userId}/orders", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }
}