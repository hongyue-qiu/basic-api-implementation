package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @Autowired
    VoteRepository voteRepository;



    @BeforeEach
    void setUp() {
        rsEventRepository.deleteAll();
        voteRepository.deleteAll();
        userRepository.deleteAll();
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
        String json = "{\"eventName\":\"event\",\"keyword\":\"valid\",\"userId\":" + user.getId() + "}";
        mockMvc.perform(post("/rs/event")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<RsEventEntity> rsEventEntities = rsEventRepository.findAll();
        assertEquals(1, rsEventEntities.size());
        assertEquals("event", rsEventEntities.get(0).getEventName());

    }

    @Test
    public void shouldReturn400AddRsEventWhenUserNotExists() throws Exception {
        String json = "{\"eventName\":\"event\",\"keyword\":\"valid\",\"userId\":1}";

        mockMvc.perform(post("/rs/event")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        List<RsEventEntity> rsEventEntities = rsEventRepository.findAll();
        assertEquals(0, rsEventEntities.size());
    }

    @Test
    public void shouldGetOneEvent() throws Exception {
        UserEntity user = UserEntity.builder()
                .name("usera")
                .gender("male")
                .age(20)
                .phone("10123456789")
                .email("123@12.cn")
                .vote(10)
                .build();
        userRepository.save(user);
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("event2")
                .keyword("key")
                .user(user)
                .build();
        rsEventRepository.save(rsEventEntity);

        mockMvc.perform(get("/rs/event/{index}", rsEventEntity.getId()))
                .andExpect(jsonPath("$.eventName", is("event2")))
                .andExpect(jsonPath("$.user.name", is("usera")));
    }

    @Test
    public void shouldUpdateEvent_when_userId_exsist_in_eventId() throws Exception {
        UserEntity user = UserEntity.builder()
                .name("usera")
                .gender("male")
                .age(20)
                .phone("10123456789")
                .email("123@12.cn")
                .vote(10)
                .Id(1)
                .build();
        userRepository.save(user);
        String json = "{\"eventName\":\"event\",\"keyword\":\"valid\",\"userId\":1}";

        mockMvc.perform(post("/rs/{eventIndex}", 1)
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<RsEventEntity> rsEventEntities = rsEventRepository.findAll();
        assertEquals("usera", rsEventEntities.get(0).getUser().getName());

    }

    @Test
    public void shouldUpdateEvent_false_when_userId_not_exsist_in_eventId() throws Exception {
        UserEntity user = UserEntity.builder()
                .name("usera")
                .gender("male")
                .age(20)
                .phone("10123456789")
                .email("123@12.cn")
                .vote(10)
                .Id(1)
                .build();
        userRepository.save(user);
        String json = "{\"eventName\":\"event\",\"keyword\":\"valid\",\"userId\":2}";

        mockMvc.perform(post("/rs/{eventIndex}", 2)
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void should_update_votenum_when_userId_vote_more_than_votnum() throws Exception {
        UserEntity user = UserEntity.builder()
                .name("usera")
                .gender("male")
                .age(20)
                .phone("10123456789")
                .email("123@12.cn")
                .vote(10)
                .build();
        userRepository.save(user);
        RsEventEntity rsEvent = RsEventEntity.builder()
                .user(user)
                .eventName("event")
                .keyword("key")
                .vote(0)
                .build();
        rsEventRepository.save(rsEvent);
        Vote vote = new Vote(user.getId(), rsEvent.getId(), LocalDateTime.now(), 6);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonVote = objectMapper.writeValueAsString(vote);

        mockMvc.perform(post("/rs/vote/{rsEventId}", rsEvent.getId())
                .content(jsonVote)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<RsEventEntity> rsEventEntities = rsEventRepository.findAll();
        assertEquals(6, rsEventEntities.get(0).getVote());
    }

    @Test
    public void should_update_false_votenum_when_userId_vote_less_than_votnum() throws Exception {
        UserEntity user = UserEntity.builder()
                .name("usera")
                .gender("male")
                .age(20)
                .phone("10123456789")
                .email("123@12.cn")
                .vote(10)
                .build();
        userRepository.save(user);
        RsEventEntity rsEvent = RsEventEntity.builder()
                .user(user)
                .eventName("event")
                .keyword("key")
                .vote(0)
                .build();
        rsEventRepository.save(rsEvent);
        Vote vote = new Vote(user.getId(), rsEvent.getId(), 15);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonVote = objectMapper.writeValueAsString(vote);

        mockMvc.perform(post("/rs/vote/{rsEventId}", rsEvent.getId())
                .content(jsonVote)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

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
    void shouldAlterResearchByIndex() throws Exception {
        UserEntity user = UserEntity.builder()
                .name("usera")
                .gender("male")
                .age(20)
                .phone("10123456789")
                .email("123@12.cn")
                .vote(10)
                .build();
        userRepository.save(user);
        RsEventEntity rsEvent = RsEventEntity.builder()
                .user(user)
                .eventName("event")
                .keyword("key")
                .vote(0)
                .build();
        rsEventRepository.save(rsEvent);

        RsEvent rsEvent1 = new RsEvent("经过修改后的第一条事件", "其他类", user.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent1);
        mockMvc.perform(put("/rs/alter/{index}", rsEvent.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/event/{index}", rsEvent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("经过修改后的第一条事件")))
                .andExpect(jsonPath("$.keyword", is("其他类")));
    }

    @Test
    void shouldCouldDeleteByIndexInSQL() throws Exception {

        UserEntity user = UserEntity.builder()
                .name("usera")
                .gender("male")
                .age(20)
                .phone("10123456789")
                .email("123@12.cn")
                .vote(10)
                .build();
        userRepository.save(user);
        RsEventEntity rsEvent = RsEventEntity.builder()
                .user(user)
                .eventName("event")
                .keyword("key")
                .vote(0)
                .build();
        rsEventRepository.save(rsEvent);
        RsEventEntity rsEvent2 = RsEventEntity.builder()
                .user(user)
                .eventName("event2")
                .keyword("key")
                .vote(0)
                .build();
        rsEventRepository.save(rsEvent2);

        mockMvc.perform(get("/rs/event/{index}", rsEvent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("event")))
                .andExpect(jsonPath("$.keyword", is("key")));

        mockMvc.perform(delete("/rs/deleteInSQL/1"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/event/{index}", rsEvent2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("event2")))
                .andExpect(jsonPath("$.keyword", is("key")));

        mockMvc.perform(get("/rs/event/{index}", rsEvent.getId()))
                .andExpect(status().isBadRequest());

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
