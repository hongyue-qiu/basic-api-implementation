package com.thoughtworks.rslist.userentity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Entity
@Table(name = "user")
public class UserEntity {

    @javax.persistence.Id
    @GeneratedValue
    Integer Id;
    @NotEmpty
    @Size(max = 8)
    private String name;
    private String gender;
    private Integer age;
    private String email;
    private String phone;
    private Integer vote = 1;
}
