package com.thoughtworks.rslist.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class User {

    @NotEmpty
    @Size(max = 8)
    private String name;
    @NotEmpty
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
