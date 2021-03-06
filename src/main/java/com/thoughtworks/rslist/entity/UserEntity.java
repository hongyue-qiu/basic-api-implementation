package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity{

    @javax.persistence.Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer Id;
    @Column(name = "name")
    private String name;
    private String gender;
    private Integer age;
    private String email;
    private String phone;
    private Integer vote;

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<RsEventEntity> rsEvents;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<VoteEntity> voteEntities;


}
