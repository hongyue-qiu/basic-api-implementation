package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.exception.CommentError;
import com.thoughtworks.rslist.exception.PostResult;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RestController
public class RsController {

//    @Autowired
    final private UserRepository userRepository;
//    @Autowired
    final private RsEventRepository rsEventRepository;
//    @Autowired
    final private VoteRepository voteRepository;

    public RsController(UserRepository userRepository, RsEventRepository rsEventRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
        this.voteRepository = voteRepository;
    }

    User user = new User("qq", "femail", 20, "123@123.com", "10123456789");
    List<User> userLists = initUserList();

    private List<User> initUserList() {
        List<User> userTempList = new ArrayList<>();
        userTempList.add(new User("qq", "femail", 20, "123@123.com", "10123456789"));
        return userTempList;

    }


    private List<RsEvent> rsList = initRsList();

//    private List<RsEvent> rsList = rsEventRepository.findAlls();

    private List<RsEvent> initRsList() {
        List<RsEvent> tempRsList = new ArrayList<>();
        tempRsList.add(new RsEvent("第一条事件", "无分类", user));
        tempRsList.add(new RsEvent("第二条事件", "无分类", user));
        tempRsList.add(new RsEvent("第三条事件", "无分类", user));

        return tempRsList;
    }

    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEvent>> getRsEventByRange(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
        if (start == null || end == null) {
            return ResponseEntity.created(null).body(rsList);
        }
        if (end > rsList.size() || start < 1) {
            throw new IndexOutOfBoundsException();
        }
        return ResponseEntity.created(null).body(rsList.subList(start - 1, end));
    }

    @GetMapping("/rs/lists")
    public ResponseEntity<List<RsEvent>> getRsEventInSQL(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
        List<RsEventEntity> rsEventEntityList = rsEventRepository.findAll();
        List<RsEvent> rsEventList = new ArrayList<>();
        Iterator<RsEventEntity> iterator = rsEventEntityList.iterator();
        while (iterator.hasNext()) {
            RsEventEntity rsEventEntity = iterator.next();
            RsEvent rsEvent = new RsEvent();
            BeanUtils.copyProperties(rsEventEntity, rsEvent);
            rsEventList.add(rsEvent);
        }

        return ResponseEntity.created(null).body(rsEventList.subList(start - 1, end));
    }

    @GetMapping("/rs/event/{index}")
    public ResponseEntity<RsEvent> getRsEventsofOne(@PathVariable int index) {
        Optional<RsEventEntity> result = rsEventRepository.findById(index);
        if (!result.isPresent()) {
            throw new IndexOutOfBoundsException();
        }

        RsEventEntity rsEventEntity = result.get();
        UserEntity user = rsEventEntity.getUser();

        return ResponseEntity.ok(RsEvent.builder()
                .eventName(rsEventEntity.getEventName())
                .keyword(rsEventEntity.getKeyword())
                .user(new User(
                        user.getName(),
                        user.getGender(),
                        user.getAge(),
                        user.getEmail(),
                        user.getPhone()))
                .build());
    }

    @ExceptionHandler({IndexOutOfBoundsException.class,
            MethodArgumentNotValidException.class})
    public ResponseEntity<CommentError> handleIndexOutOfBoundsException(Exception ex) {

        if (ex instanceof MethodArgumentNotValidException) {
            CommentError commentError = new CommentError();
            commentError.setError("invalid param");
            return ResponseEntity.badRequest().body(commentError);
        }

        CommentError commentError = new CommentError();
        commentError.setError("invalid index");
        return ResponseEntity.badRequest().body(commentError);
    }

    @PostMapping("/rs/event")
    public ResponseEntity addRsEvent(@Valid @RequestBody RsEvent rsEvent) throws Exception {
        if (!userRepository.existsById(rsEvent.getUserId())) {
            return ResponseEntity.badRequest().build();
        }

        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword())
                .user(UserEntity.builder()
                        .Id(rsEvent.getUserId())
                        .build())
                .build();
        rsEventRepository.save(rsEventEntity);

        return ResponseEntity.created(null).header("index", String.valueOf(rsList.indexOf(rsEvent))).build();
    }

    @PutMapping("/rs/alter/{index}")
    public ResponseEntity alterResearch(@PathVariable int index, @RequestBody RsEvent rsEvent) {
        Optional<RsEventEntity> rsEventEntity = rsEventRepository.findById(index);
        RsEventEntity rsEvents = rsEventEntity.get();
        if (rsEvent.getEventName() != null) {
            rsEvents.setEventName(rsEvent.getEventName());
        }
        if (rsEvent.getKeyword() != null) {
            rsEvents.setKeyword(rsEvent.getKeyword());
        }
        rsEventRepository.save(rsEvents);

        return ResponseEntity.created(null).header("index", String.valueOf(rsList.indexOf(rsEvent))).build();
    }


    @DeleteMapping("/rs/deleteInSQL/{index}")
    public ResponseEntity<PostResult> deleteResearchInSQL(@PathVariable int index) {
        rsEventRepository.deleteById(index);
        return ResponseEntity.created(null).header("index", String.valueOf(index)).build();
    }

    @PutMapping("/rs/list/has_user_name")
    public ResponseEntity addUserName(@RequestBody List<RsEvent> rsEventList) {
        List<RsEvent> tempList = rsList;
        for (int i = 0; i < tempList.size(); i++) {
            RsEvent rsEvent = tempList.get(i);
            if (rsEvent.getUser() == null)
                rsEvent.setUserName(user.getName());
            rsEvent.setUser(user);
            return ResponseEntity.created(null).header("index", String.valueOf(i)).build();
        }

        return ResponseEntity.created(null).build();

    }

    @PostMapping("/rs/{eventIndex}")
    public ResponseEntity updateEventUserMessage(@RequestBody RsEvent rsEvent) {

        if (!userRepository.existsById(rsEvent.getUserId())) {
            return ResponseEntity.badRequest().build();
        }

        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword())
                .user(UserEntity.builder()
                        .Id(rsEvent.getUserId())
                        .build())
                .build();
        rsEventRepository.save(rsEventEntity);

        return ResponseEntity.created(null).build();
    }

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity updateEventVoteNum(@RequestBody(required = false) Vote vote, @PathVariable Integer rsEventId) {
        Optional<RsEventEntity> rsEventEntity = rsEventRepository.findById(rsEventId);
        Optional<UserEntity> userEntity = userRepository.findById(vote.getUserId());
        if (!rsEventEntity.isPresent() || !userEntity.isPresent() ||
                vote.getVoteNum() > userEntity.get().getVote()) {
            return ResponseEntity.badRequest().build();
        }
        VoteEntity voteEntity = VoteEntity.builder()
                .localDateTime(vote.getLocalDateTime())
                .num(vote.getVoteNum())
                .rsEvent(rsEventEntity.get())
                .user(userEntity.get())
                .build();
        voteRepository.save(voteEntity);
        UserEntity user = userEntity.get();
        user.setVote(user.getVote() - vote.getVoteNum());
        userRepository.save(user);
        RsEventEntity rsEvent = rsEventEntity.get();
        rsEvent.setVote(rsEvent.getVote() + vote.getVoteNum());
        rsEventRepository.save(rsEvent);

        return ResponseEntity.created(null).build();
    }


}
