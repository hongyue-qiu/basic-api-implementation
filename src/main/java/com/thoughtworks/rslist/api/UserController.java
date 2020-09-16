package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.exception.PostResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    List<User> userList = new ArrayList<>();

    @PostMapping("/user/register")
    public ResponseEntity<PostResult> registUser(@Valid @RequestBody User user){
        userList.add(user);
        PostResult postResult = new PostResult();
        int num = userList.indexOf(user);
        postResult.setIndex(num);
        return ResponseEntity.created(null).body(postResult);
    }

}
