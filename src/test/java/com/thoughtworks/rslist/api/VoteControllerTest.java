package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class VoteControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    VoteRepository voteRepository;

    private UserEntity userEntity;
    private RsEventEntity rsEventEntity;
    private VoteEntity voteEntity;

    LocalDateTime localDateTime;

    @BeforeEach
    void setUp(){
        localDateTime = localDateTime.now();
        setData();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        voteRepository.deleteAll();
    }

//    @Test
//    void shouldGetVoteIdByUserIdAndRsEventId() throws Exception {
//        mockMvc.perform(get("/vote")
//                .param("userId", String.valueOf(userEntity.getId()))
//                .param("rsEventId", String.valueOf(rsEventEntity.getId())))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[0].userId", is(userEntity.getId())))
//                .andExpect(jsonPath("$[0].rsEventId", is(userEntity.getId())))
//                .andExpect(jsonPath("$[0].voteNum", is(1)));
//    }

    @Test
    void should_get_votes_in_time_range_when_get_specify_time_range_votes() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("user")
                .age(23)
                .gender("male")
                .email("12@12.cn")
                .phone("10123456789")
                .vote(10)
                .build();
        userRepository.save(userEntity);

        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("event")
                .user(userEntity)
                .keyword("key")
                .build();
        rsEventRepository.save(rsEventEntity);

        VoteEntity voteEntity = VoteEntity.builder()
                .localDateTime(LocalDateTime.now())
                .user(userEntity)
                .rsEvent(rsEventEntity)
                .num(4)
                .build();

        voteRepository.save(voteEntity);

        voteEntity = VoteEntity.builder()
                .localDateTime(LocalDateTime.now())
                .user(userEntity)
                .rsEvent(rsEventEntity)
                .num(4)
                .build();

        voteRepository.save(voteEntity);
        mockMvc.perform(get("/vote/recorde")
                .param("startTime", String.valueOf(LocalDateTime.now()))
                .param("endTime", String.valueOf(LocalDateTime.now())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].voteNum", is(1)))
                .andExpect(jsonPath("$[0].userId", is(1)));

    }


    private void setData(){
        userEntity = UserEntity.builder()
                .name("user")
                .gender("male")
                .age(20)
                .phone("10123456789")
                .email("12@12.cn")
                .vote(10)
                .build();
        userRepository.save(userEntity);
        rsEventEntity = RsEventEntity.builder()
                .eventName("event")
                .keyword("key")
                .user(userEntity)
                .build();
        rsEventRepository.save(rsEventEntity);
        VoteEntity vote = VoteEntity.builder()
                .user(userEntity)
                .rsEvent(rsEventEntity)
                .num(1)
                .localDateTime(LocalDateTime.now())
                .build();
        voteRepository.save(vote);
        vote = VoteEntity.builder()
                .user(userEntity)
                .rsEvent(rsEventEntity)
                .num(2)
                .localDateTime(LocalDateTime.now())
                .build();
        voteRepository.save(vote);
        vote = VoteEntity.builder()
                .user(userEntity)
                .rsEvent(rsEventEntity)
                .num(3)
                .localDateTime(LocalDateTime.now())
                .build();
        voteRepository.save(vote);

    }


}
