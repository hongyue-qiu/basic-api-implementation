package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RsListApplicationTests {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
    }

    @Test
    public void shouldAddRsEventWhenUserExists() throws Exception {
        UserEntity user = UserEntity.builder()
                .name("usera")
                .gender("male")
                .age(20)
                .phone("10123456789")
                .email("123@12.cn")
                .vote(10)
                .build();
        userRepository.save(user);
        String json = "{\"eventName\":\"event\",\"keyword\":\"valid\",\"userId\":" + user.getId()+"}";
        mockMvc.perform(post("/rs/event")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<RsEventEntity> rsEventEntities = rsEventRepository.findAll();
        assertEquals(1,rsEventEntities.size());
        assertEquals("event",rsEventEntities.get(0).getEventName());


    }

    @Test
    void contextLoads() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无分类")));

    }

    @Test
    void geOneOfEvent() throws Exception {
        mockMvc.perform(get("/rs/event/1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyword", is("无分类")));
        mockMvc.perform(get("/rs/event/2"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyword", is("无分类")));
        mockMvc.perform(get("/rs/event/3"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyword", is("无分类")));

    }

    @Test
    void shouldAddOneEvent() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", hasSize(3)));
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("index", "3"));
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无分类")))
                .andExpect(jsonPath("$[3].eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$[3].keyword", is("经济")));

    }

    @Test
    void shouldCouldModifyResearchByIndex() throws Exception {
        RsEvent eventIndexInOneModify = new RsEvent("经过修改后的第一条事件", "其他类");
        RsEvent eventIndexInTwoModify = new RsEvent("经过修改后的第二条事件", "");
        RsEvent eventIndexInThreeModify = new RsEvent("", "其他类");

        String eventIndexOneJsonStringModified = convertResearchToJsonString(eventIndexInOneModify);
        String eventIndexTwoJsonStringModified = convertResearchToJsonString(eventIndexInTwoModify);
        String eventIndexThreeJsonStringModified = convertResearchToJsonString(eventIndexInThreeModify);


        performPut("/rs/modify/1", eventIndexOneJsonStringModified);
        performPut("/rs/modify/2", eventIndexTwoJsonStringModified);
        performPut("/rs/modify/3", eventIndexThreeJsonStringModified);


        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isCreated())

                .andExpect(jsonPath("$[0].eventName", is("经过修改后的第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("其他类")))
                .andExpect(jsonPath("$[1].eventName", is("经过修改后的第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("其他类")));
    }

    private String convertResearchToJsonString(RsEvent rsEvent) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(rsEvent);
    }

    private void performPut(String url, String jsonString) throws Exception {
        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldCouldDeleteByIndex() throws Exception {

        mockMvc.perform(delete("/rs/delete/1"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无分类")));
    }

    @Test
    void shouldAddNameIfUsernameEmpty() throws Exception {
        List<RsEvent> rsEventList = new ArrayList<>();
        RsEvent rsEvent = new RsEvent("第一条事件", "无分类", "");
        rsEventList.add(rsEvent);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEventList);
        mockMvc.perform(put("/rs/list/has_user_name")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无分类")))
                .andExpect(jsonPath("$[0].username", is("qq")));
    }

    @Test
    void should_no_add_a_re_event_when_name_empty() throws Exception {
        RsEvent rsEvent = new RsEvent(null, "异常");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

}
