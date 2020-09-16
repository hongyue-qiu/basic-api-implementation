package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.exception.PostResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    List<User> userLists = initUserList();

    private List<User> initUserList() {
        List<User> userTempList = new ArrayList<>();
        userTempList.add(new User("qq", "femail", 20, "123@123.com", "10123456789"));
        return userTempList;
    }

    @PostMapping("/user/register")
    public ResponseEntity<PostResult> registUser(@Valid @RequestBody User user) {
        userLists.add(user);
        PostResult postResult = new PostResult();
        int num = userLists.indexOf(user);
        postResult.setIndex(num);
        return ResponseEntity.created(null).body(postResult);
    }

    @GetMapping("/rs/userList")
    public ResponseEntity<List<User>> getRsEventByRange() {
//        if (start == null || end == null) {
//            return ResponseEntity.created(null).body(userList);
//        }
        return ResponseEntity.created(null).body(userLists);
    }

}
