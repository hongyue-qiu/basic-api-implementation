package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void user_register() throws Exception {
        User user = new User("qqq","男",19,"123@123.com","10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUserStr = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register")
                .content(jsonUserStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void user_name_not_mull() throws Exception {
        User user = new User("","男",19,"123@123.com","10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUserStr = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register")
                .content(jsonUserStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void user_name_not_more_8() throws Exception {
        User user = new User("123456789","男",19,"123@123.com","10123456789");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUserStr = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register")
                .content(jsonUserStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }




}