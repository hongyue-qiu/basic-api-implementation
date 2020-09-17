package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.exception.CommentError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    public ResponseEntity registUser(@Valid @RequestBody User user) {
        userLists.add(user);
        if (user.getName() == null || user.getGender() == null){

        }

        return ResponseEntity.created(null).header("index",String.valueOf(userLists.indexOf(user))).build();
    }

    @GetMapping("/rs/userList")
    public ResponseEntity<List<User>> getRsEventByRange() {
        return ResponseEntity.created(null).body(userLists);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<CommentError> handle(Exception ex){

        CommentError commentError = new CommentError();
        commentError.setError("invalid user");
        return ResponseEntity.badRequest().body(commentError);

    }

}
