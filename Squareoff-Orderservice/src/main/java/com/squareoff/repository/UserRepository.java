package com.squareoff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.squareoff.model.UserEntity;

@Repository
public interface UserRepository  extends JpaRepository<UserEntity, String>{

    @Query(value = "select en from UserEntity as en where en.userMobie=?1")
    UserEntity findByuserMobie(String userMobie);
 
}
