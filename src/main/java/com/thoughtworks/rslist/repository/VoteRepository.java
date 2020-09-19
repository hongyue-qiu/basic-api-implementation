package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteRepository extends CrudRepository<VoteEntity,Integer> {
    List<VoteEntity> findAll();
//    List<VoteEntity> findAllByUserIdAndRsEventId(int userId,int rsEventId);
    List<VoteEntity> findAllByLocalDateTimeBetween(LocalDateTime start,LocalDateTime end);
    List<VoteEntity> findAllByRsEventId(Integer xxx);
//    List<VoteEntity> findAllByUserId(Integer Id);
}
