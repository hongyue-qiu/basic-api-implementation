package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.User;
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
    public ResponseEntity registUser(@Valid @RequestBody User user){
        userList.add(user);
        return ResponseEntity.created(null).build();
    }

}
