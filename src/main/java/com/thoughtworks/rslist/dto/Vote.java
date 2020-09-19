package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vote {
    private int userId;
    private int rsEventId;
    private LocalDateTime localDateTime;
    private int voteNum;

    public Vote(int userId, int rsEventId, int voteNum) {
        this.userId = userId;
        this.rsEventId = rsEventId;
        this.voteNum = voteNum;
    }

    public Vote(int userId, int rsEventId, LocalDateTime now) {
        this.userId = userId;
        this.rsEventId = rsEventId;
    }


}
