package com.thoughtworks.rslist.dto;

import lombok.Data;

@Data
public class RsEvent {
    public RsEvent(String eventName, String keyword, User eventUser) {
        this.eventName = eventName;
        this.keyword = keyword;
        this.eventUser = eventUser;
    }

    private String eventName;
    private String keyword;
    private String userName;
    User eventUser;

    public String getUsername() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public RsEvent() {
    }

    public RsEvent(String eventName, String keyword) {
        this.eventName = eventName;
        this.keyword = keyword;
    }

    public RsEvent(String eventName, String keyword, String userName) {
        this.eventName = eventName;
        this.keyword = keyword;
        this.userName = userName;
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

    public User getEventUser() {
        return eventUser;
    }

    public void setEventUser(User eventUser) {
        this.eventUser = eventUser;
    }
}
