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
import tw.iancheng.giftcardsystem.dto.user.UserRegisterRequest;
import tw.iancheng.giftcardsystem.model.User;
import tw.iancheng.giftcardsystem.repository.UserRepository;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 註冊新帳號
    @Test
    public void register_success() throws Exception {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .email("test1@gcs.com").password("P@ssw0rd")
                .build();

        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.email", equalTo("test1@gcs.com")))
                .andExpect(jsonPath("$.password", notNullValue()));

        // 檢查資料庫中的密碼不為明碼
        User user = userRepository.getUserByEmail(userRegisterRequest.getEmail());
        assertNotEquals(userRegisterRequest.getPassword(), user.getPassword());
    }

    @Test
    public void register_invalidEmailFormat() throws Exception {
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .email("test invalid email format").password("P@ssw0rd")
                .build();

        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Test
    public void register_emailAlreadyExist() throws Exception {
        // 先註冊一個帳號
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .email("test2@gcs.com").password("P@ssw0rd")
                .build();

        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201));

        // 再次使用同個 email 註冊
        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }
}