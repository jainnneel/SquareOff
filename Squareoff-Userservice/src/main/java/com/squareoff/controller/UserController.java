package com.squareoff.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.squareoff.dto.MoneyAddRequest;
import com.squareoff.dto.PaymentRequest;
import com.squareoff.dto.UserDto;
import com.squareoff.model.UserEntity;
import com.squareoff.response.ResponseEntity;
import com.squareoff.serviceImpl.UserServiceImplementation;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceImplementation userImplementation;
    
    @PostMapping("/register/{id}")
    public String dsda(@PathVariable("id") long id) {
        return "Dsad";
    }
    
    @GetMapping("/id/{id}")
    public String dsdad(@PathVariable("id") long id) {
        return "Dsad";
    }
    
    @GetMapping( value =  "/userInfo/{uid}/{mobile}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserEntity userInfo(@PathVariable("uid") long id,@PathVariable("mobile") String mobile) {
        
        return userImplementation.getUserInfo(id,mobile);
    }
    
    @GetMapping( value =  "/userInfo/{uid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserEntity userInfo(@PathVariable("uid") String id) {
        
        return userImplementation.getUserInfo(id);
    }
    
    @PostMapping(value = "/register/user",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUser(@RequestBody UserDto userDto,BindingResult result) {
        ResponseEntity responseEntity = new ResponseEntity();
        
        ValidationUtils.rejectIfEmptyOrWhitespace(result, "userName", "username is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(result, "userMobie", "userMobie is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(result, "userPassword", "userPassword is required");
        
        if (result.hasErrors()) {
            responseEntity.setData(result.getAllErrors().toString());
            responseEntity.setHttpStatus(HttpStatus.NOT_ACCEPTABLE);
            responseEntity.setStatus("failed");
            return responseEntity;
        }
        
        String output = userImplementation.createUser(userDto);
        responseEntity.setData(output);
        responseEntity.setHttpStatus(HttpStatus.OK);
        responseEntity.setStatus("success");
        return responseEntity;
    }
    
    @PostMapping(value = "/addmoney",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addMoneyRequest(@RequestBody MoneyAddRequest addRequest,BindingResult result) {
        ResponseEntity responseEntity = new ResponseEntity();
        
        ValidationUtils.rejectIfEmptyOrWhitespace(result, "amount", "Amount is required");
        if (result.hasErrors()) {
            responseEntity.setData(result.getAllErrors().toString());
            responseEntity.setHttpStatus(HttpStatus.NOT_ACCEPTABLE);
            responseEntity.setStatus("failed");
            return responseEntity;
        }
        SecurityContext context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        String orderId = userImplementation.addpaymentrequest(addRequest,userId);
        
        if (orderId!=null) {
            responseEntity.setData(orderId);
            responseEntity.setHttpStatus(HttpStatus.OK);
            responseEntity.setStatus("success");
            return responseEntity;
        }else {
            responseEntity.setData("something went wrong");
            responseEntity.setHttpStatus(HttpStatus.OK);
            responseEntity.setStatus("success");
            return responseEntity;
        }
    }
    
    @PostMapping(value = "payment",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity completePayment(@RequestBody PaymentRequest paymetreq,BindingResult results) {
        ResponseEntity responseEntity = new ResponseEntity();
        ValidationUtils.rejectIfEmptyOrWhitespace(results, "orderId", "orderId is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(results, "paymentId", "paymentId is required");
        
        if (results.hasErrors()) {
            responseEntity.setStatus("failed");
            responseEntity.setData(results.getAllErrors().toString());
            responseEntity.setHttpStatus(HttpStatus.NOT_ACCEPTABLE);
            return responseEntity;
        }
        SecurityContext context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        if (userImplementation.verifyandaddUser(paymetreq,userId)) {
            responseEntity.setData("payment added to your wallet");
            responseEntity.setHttpStatus(HttpStatus.OK);
            responseEntity.setStatus("success");
            return responseEntity;
        }else {
            responseEntity.setData("Something went wrong");
            responseEntity.setHttpStatus(HttpStatus.OK);
            responseEntity.setStatus("failed");
            return responseEntity;
        }
    }
    
    
}



















