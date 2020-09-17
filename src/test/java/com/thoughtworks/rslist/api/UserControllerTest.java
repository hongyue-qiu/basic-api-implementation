package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void contextLoads() throws Exception {
        mockMvc.perform(get("/rs/userList"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].name", is("qq")))
                .andExpect(jsonPath("$[0].gender", is("femail")))
                .andExpect(jsonPath("$[0].age", is(20)))
                .andExpect(jsonPath("$[0].email", is("123@123.com")))
                .andExpect(jsonPath("$[0].phone", is("10123456789")));

    }

    @Test
    void user_register() throws Exception {
        User user = new User("qqq", "男", 19, "994059641@qq.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUserStr = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register")
                .content(jsonUserStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void entityuser_register() throws Exception {
        User user = new User("qqq", "男", 19, "994059641@qq.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUserStr = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/userEntityRegister")
                .content(jsonUserStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<UserEntity> users = userRepository.findAll();
        assertEquals(1, users.size());
        assertEquals("qqq", users.get(0).getName());


    }

    @Test
    void entityuser_delete() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("haah")
                .email("123@123")
                .age(20)
                .gender("male")
                .phone("10123456789")
                .vote(10)
                .build();
        userRepository.save(userEntity);


        mockMvc.perform(delete("/user/{id}", userEntity.getId()))
                .andExpect(status().isNoContent());

        List<UserEntity> users = userRepository.findAll();
        assertEquals(0, users.size());


    }


    @Test
    void user_name_not_mull() throws Exception {
        User user = new User("", "男", 19, "123@123.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUserStr = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register")
                .content(jsonUserStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    void user_name_not_more_8() throws Exception {
        User user = new User("123456789", "男", 19, "123@123.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUserStr = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register")
                .content(jsonUserStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    void user_gender_not_null() throws Exception {
        User user = new User("12345678", "", 19, "123@123.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUserStr = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register")
                .content(jsonUserStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    void user_age_not_null() throws Exception {
        User user = new User("12345678", "男", null, "123@123.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUserStr = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register")
                .content(jsonUserStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    void user_age_18_more() throws Exception {
        User user = new User("qqq", "男", 17, "123@123.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUserStr = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register")
                .content(jsonUserStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    void user_age_100_less() throws Exception {
        User user = new User("qqq", "男", 117, "123@123.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUserStr = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register")
                .content(jsonUserStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    void user_email_valid() throws Exception {
        User user = new User("qqq", "男", 19, "1123.com", "10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUserStr = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register")
                .content(jsonUserStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    void user_phone_number_not_start_1() throws Exception {
        User user = new User("qqq", "男", 19, "11@23.com", "20123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUserStr = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register")
                .content(jsonUserStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    void user_phone_number_not_length_is_11() throws Exception {
        User user = new User("qqq", "男", 19, "11@23.com", "1123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUserStr = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register")
                .content(jsonUserStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }


}