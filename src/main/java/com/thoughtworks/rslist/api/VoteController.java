package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VoteController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    private final VoteRepository voteRepository;


    public VoteController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

//    @GetMapping("/vote")
//    public List<Vote> getVotes(@RequestParam int userId,@RequestParam int rsEventId){
//        List<VoteEntity> votes = voteRepository.findAllByUserIdAndRsEventId(userId,rsEventId);
//        return votes.stream().map(vote->Vote.builder()
//                .userId(vote.getUser().getId())
//                .rsEventId(vote.getRsEvent().getId())
//                .voteNum(vote.getNum())
//                .localDateTime(vote.getLocalDateTime())
//                .build()
//        ).collect(Collectors.toList());
//    }
    @GetMapping("/vote/recorde")
    public ResponseEntity<List<Vote>> getVoteRecordByRange
            (@RequestParam
             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
             @RequestParam
             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
//        List<VoteEntity> voteEntities = voteRepository.findAllByLocalDateTimeBetween(startTime,endTime);
//        return voteEntities.stream().map(vote -> Vote.builder()
//                .userId(vote.getUser().getId())
//                .rsEventId(vote.getRsEvent().getId())
//                .voteNum(vote.getNum())
//                .localDateTime(vote.getLocalDateTime())
//                .build()).collect(Collectors.toList());
//        return ResponseEntity.ok().build();
        List<VoteEntity> voteEntityList = voteRepository.findAllByLocalDateTimeBetween(startTime, endTime);
        List<Vote> voteDtoList = voteEntityList.stream()
                .map(voteEntity -> Vote.builder()
                        .localDateTime(voteEntity.getLocalDateTime())
                        .rsEventId(voteEntity.getRsEvent().getId())
                        .userId(voteEntity.getUser().getId())
                        .voteNum(voteEntity.getNum())
                        .build())
                .collect(Collectors.toList());
        return  ResponseEntity.ok().body(voteDtoList);
    }


}
