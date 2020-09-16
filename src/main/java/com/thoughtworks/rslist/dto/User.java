package com.thoughtworks.rslist.dto;

import lombok.Data;

@Data
public class User {
    private String name;
    private String gender;
    private Integer age;
    private String email;
    private String phone;
    private Integer vote = 1;

    public User(String name, String gender, Integer age, String email, String phone) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }
}
