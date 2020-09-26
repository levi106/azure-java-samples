package com.samples.aiv2.repository;

import com.samples.aiv2.entity.Message;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
    
}
