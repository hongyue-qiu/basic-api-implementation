package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoteController {
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    RsEventRepository rsEventRepository;
    private final VoteRepository voteRepository;

    public VoteController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }
}
