package com.squareoff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.squareoff.model.UserEntity;
import com.squareoff.model.UserHoldingModel;

@Repository
public interface UserHoldingRepo extends JpaRepository<UserHoldingModel, Long> {

    @Query(value = "select uh from UserHoldingModel as uh where uh.userEntity=?1")
    List<UserHoldingModel> getAllHoldingOfUser(UserEntity userEntity); 

    
}
