package com.thoughtworks.rslist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rs_event")
@Data
public class RsEventEntity {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "name")
    private String eventName;
    private String keyword;
    private int userId;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private UserEntity user;

    public RsEventEntity(String eventName, String keyword) {
        this.eventName = eventName;
        this.keyword = keyword;
    }

    public RsEventEntity(String eventName, String keyword, int userId) {
        this.eventName = eventName;
        this.keyword = keyword;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @JsonIgnore
    public int getUserId() {
        return userId;
    }

    @JsonProperty
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
