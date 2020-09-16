package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.exception.PostResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {



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
        tempRsList.add(new RsEvent("第一条事件", "无分类"));
        tempRsList.add(new RsEvent("第二条事件", "无分类"));
        tempRsList.add(new RsEvent("第三条事件", "无分类"));
        return tempRsList;
    }

    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEvent>> getRsEventByRange(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
        if (start == null || end == null) {
            return ResponseEntity.created(null).body(rsList);
        }
        return ResponseEntity.created(null).body(rsList.subList(start - 1, end));
    }

    @GetMapping("/rs/event/{index}")
    public ResponseEntity<RsEvent> getRsEventByRange(@PathVariable int index) {
        return ResponseEntity.created(null).body(rsList.get(index - 1));
    }

    @PostMapping("/rs/event")
    public ResponseEntity<PostResult> addRsEvent(@RequestBody RsEvent rsEvent) throws JsonProcessingException {
        rsList.add(rsEvent);

        return getPostResultResponseEntity();
    }

    @PutMapping("/rs/modify/{index}")
    public ResponseEntity<PostResult> modifyResearch(@PathVariable int index, @RequestBody RsEvent rsEvent) {

        RsEvent reEventModified = rsList.get(index - 1);
        if (!rsEvent.getEventName().isEmpty()) {
            reEventModified.setEventName(rsEvent.getEventName());
        }
        if (!rsEvent.getKeyword().isEmpty()) {
            reEventModified.setKeyword(rsEvent.getKeyword());
        }

        rsList.set(index - 1, reEventModified);

        return getPostResultResponseEntity();
    }

    @DeleteMapping("/rs/delete/{index}")
    public ResponseEntity<PostResult> deleteResearch(@PathVariable int index) {
        rsList.remove(index - 1);
        return getPostResultResponseEntity();
    }

    @PutMapping("/rs/list/has_user_name")
    public ResponseEntity<PostResult> addUserName(@RequestBody List<RsEvent> rsEventList){
        List<RsEvent> tempList = rsList;
        for (int i = 0; i < tempList.size(); i++) {
            RsEvent rsEvent =tempList.get(i);
            //if (rsEvent.getUsername() == null)
            rsEvent.setUserName(user.getName());
        }

        return getPostResultResponseEntity();

    }

    private ResponseEntity<PostResult> getPostResultResponseEntity() {
        PostResult postResult = new PostResult();
        int num = rsList.indexOf(user);
        postResult.setIndex(num);
        return ResponseEntity.created(null).body(postResult);
    }

}
