package com.thoughtworks.rslist.dto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class User {

    @NotEmpty
    @Size(max = 8)
    private String name;
    @NotEmpty
    private String gender;
    @NotNull
    @Min(18)
    @Max(100)
    private Integer age;
    @Email
    private String email;
    @Pattern(regexp = "^1\\d{10}$")
    private String phone;
    private Integer vote = 1;

    public User(String name, String gender, Integer age, String email, String phone) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
