package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
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
