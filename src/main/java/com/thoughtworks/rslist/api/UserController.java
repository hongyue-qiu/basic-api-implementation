package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.CommentError;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {


    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;


    public UserController(UserRepository userRepository, RsEventRepository rsEventRepository){
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
    }
    List<User> userLists = initUserList();

    private List<User> initUserList() {
        List<User> userTempList = new ArrayList<>();
        userTempList.add(new User("qq", "femail", 20, "123@123.com", "10123456789"));
        return userTempList;
    }

    @PostMapping("/user/register")
    public ResponseEntity registUser(@Valid @RequestBody User user) {
        userLists.add(user);
        return ResponseEntity.created(null).header("index", String.valueOf(userLists.indexOf(user))).build();
    }

    @PostMapping("/user/userEntityRegister")
    public ResponseEntity registEntityUser(@Valid @RequestBody User user) {
        UserEntity userEntity = UserEntity.builder()
                                .name(user.getName())
                                .email(user.getEmail())
                                .age(user.getAge())
                                .gender(user.getGender())
                                .phone(user.getPhone())
                                .vote(user.getVote())
                                .build();
        userRepository.save(userEntity);
        return ResponseEntity.created(null).header("index", String.valueOf(userLists.indexOf(user))).build();
    }

    @DeleteMapping("/user/{id}")
    @Transactional
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
    }

    @GetMapping("/rs/userList")
    public ResponseEntity<List<User>> getRsEventByRange() {
        return ResponseEntity.created(null).body(userLists);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<CommentError> handle(Exception ex) {

        CommentError commentError = new CommentError();
        commentError.setError("invalid user");
        return ResponseEntity.badRequest().body(commentError);

    }

}
