package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.CommentError;
import com.thoughtworks.rslist.exception.PostResult;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class RsController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    public RsController(UserRepository userRepository, RsEventRepository rsEventRepository){
        this.userRepository = userRepository;
        this.userRepository = userRepository;

    }

    User user = new User("qq","femail",20,"123@123.com","10123456789");
    List<User> userLists = initUserList();
    private List<User> initUserList() {
        List<User> userTempList = new ArrayList<>();
        userTempList.add(new User("qq","femail",20,"123@123.com","10123456789"));
        return userTempList;

    }


    private List<RsEvent> rsList = initRsList();
    private List<RsEvent> initRsList() {
        List<RsEvent> tempRsList = new ArrayList<>();
        tempRsList.add(new RsEvent("第一条事件", "无分类",user));
        tempRsList.add(new RsEvent("第二条事件", "无分类",user));
        tempRsList.add(new RsEvent("第三条事件", "无分类",user));

        return tempRsList;
    }

    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEvent>> getRsEventByRange(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
        if (start == null || end == null) {
            return ResponseEntity.created(null).body(rsList);
        }if (end > rsList.size() || start < 1){
            throw new IndexOutOfBoundsException();
        }
        return ResponseEntity.created(null).body(rsList.subList(start - 1, end));
    }

    @GetMapping("/rs/event/{index}")
    public ResponseEntity<RsEvent> getRsEventsofOne(@PathVariable int index) {
        Optional<RsEventEntity> result = rsEventRepository.findById(index);
        if (!result.isPresent()){
            throw new IndexOutOfBoundsException();
        }

        RsEventEntity rsEventEntity = result.get();
        UserEntity user = rsEventEntity.getUser();
//        userRepository.findAllById(rsEventRepository.getUserId()).get();

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
    public ResponseEntity<CommentError> handleIndexOutOfBoundsException(Exception ex){

        if (ex instanceof MethodArgumentNotValidException){
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
        if (!userRepository.existsById(rsEvent.getUserId())){
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

        return ResponseEntity.created(null).header("index",String.valueOf(rsList.indexOf(rsEvent))).build();
    }

    @PutMapping("/rs/modify/{index}")
    public ResponseEntity modifyResearch(@PathVariable int index, @RequestBody RsEvent rsEvent) {

        RsEvent reEventModified = rsList.get(index - 1);
        if (!rsEvent.getEventName().isEmpty()) {
            reEventModified.setEventName(rsEvent.getEventName());
        }
        if (!rsEvent.getKeyword().isEmpty()) {
            reEventModified.setKeyword(rsEvent.getKeyword());
        }

        rsList.set(index - 1, reEventModified);

        return ResponseEntity.created(null).header("index",String.valueOf(rsList.indexOf(rsEvent))).build();
    }

    @DeleteMapping("/rs/delete/{index}")
    public ResponseEntity<PostResult> deleteResearch(@PathVariable int index) {
        rsList.remove(index - 1);
        return ResponseEntity.created(null).header("index",String.valueOf(index)).build();
    }

    @PutMapping("/rs/list/has_user_name")
    public ResponseEntity addUserName(@RequestBody List<RsEvent> rsEventList){
        List<RsEvent> tempList = rsList;
        for (int i = 0; i < tempList.size(); i++) {
            RsEvent rsEvent =tempList.get(i);
            if (rsEvent.getUser() == null)
            rsEvent.setUserName(user.getName());
            rsEvent.setUser(user);
            return ResponseEntity.created(null).header("index",String.valueOf(i)).build();
        }

        return ResponseEntity.created(null).build();

    }


}
