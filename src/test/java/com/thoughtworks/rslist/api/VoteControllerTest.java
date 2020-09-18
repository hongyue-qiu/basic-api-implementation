package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

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
//    private VoteEntity voteEntity;

    @BeforeEach
    void setUp(){
        setData();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        voteRepository.deleteAll();
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
//        VoteEntity vote = VoteEntity.builder()
//                .user(userEntity)
//                .rsEvent(rsEventEntity)
//                .num(1)
//                .localDateTime(LocalDateTime.now())
//                .build();
//        voteRepository.save(vote);
//        vote = VoteEntity.builder()
//                .user(userEntity)
//                .rsEvent(rsEventEntity)
//                .num(2)
//                .localDateTime(LocalDateTime.now())
//                .build();
//        voteRepository.save(vote);
    }


}
