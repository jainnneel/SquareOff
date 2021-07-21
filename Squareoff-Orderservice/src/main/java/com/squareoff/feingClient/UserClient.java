package com.squareoff.feingClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.squareoff.model.UserEntity;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {
    
    @GetMapping(value = "/users/userInfo/{uid}/{mobile}")
    UserEntity getUserInfo(@PathVariable Long uid,@PathVariable String mobile);
    
    @GetMapping(value = "/users/userInfo/{uid}")
    UserEntity getUserInfo(@PathVariable String uid);

}
