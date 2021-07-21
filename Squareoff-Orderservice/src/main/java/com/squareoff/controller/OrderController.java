package com.squareoff.controller;

import java.security.Principal;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
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

import com.squareoff.dto.OrederRequestDto;
import com.squareoff.dto.ResponseEntity;
import com.squareoff.feingClient.StockShowingClient;
import com.squareoff.feingClient.UserClient;
import com.squareoff.model.OrderRequest;
import com.squareoff.model.StockEntity;
import com.squareoff.model.UserEntity;
import com.squareoff.serviceImpl.OrderServiceImpl;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private StockShowingClient stockClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private OrderServiceImpl orderImpl;

    @GetMapping("/{id}")
    public String data(@PathVariable("id") Long id, Principal principal) {
        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) principal;
        AccessToken accessToken = keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getToken();

        System.out.println(accessToken.getGivenName());
        return accessToken.getGivenName();
    }

    @PostMapping(value = "/orderreq", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity savaOrder(@RequestBody OrederRequestDto requestDto, BindingResult result,
            Principal currentUser) {
        ResponseEntity responseEntity = new ResponseEntity();

        ValidationUtils.rejectIfEmptyOrWhitespace(result, "type", "required");
        ValidationUtils.rejectIfEmptyOrWhitespace(result, "stockId", "required");
        ValidationUtils.rejectIfEmptyOrWhitespace(result, "price", "required");
        ValidationUtils.rejectIfEmptyOrWhitespace(result, "quantity", "required");

        if (result.hasErrors()) {
            responseEntity.setData(result.getAllErrors().toString());
            responseEntity.setHttpStatus(org.springframework.http.HttpStatus.BAD_REQUEST);
            responseEntity.setStatus("failed");
            return responseEntity;
        }

        try {
            SecurityContext context = SecurityContextHolder.getContext();
            String userName = context.getAuthentication().getName();

            UserEntity userEntity = userClient.getUserInfo(userName);

            StockEntity stockEntity = stockClient.getStockInfo(requestDto.getStockId());

            OrderRequest applyOrder = orderImpl.applyOrder(userEntity, stockEntity, requestDto);
            if (applyOrder != null) {
                if (applyOrder.getOrderId().equals(-1L)) {
                    responseEntity.setHttpStatus(HttpStatus.OK);
                    responseEntity.setStatus("success");
                    return responseEntity;
                }else {
                    responseEntity.setHttpStatus(HttpStatus.OK);
                    responseEntity.setStatus("success");
                    responseEntity.setData(applyOrder);
                    return responseEntity;
                }
            }else {
                responseEntity.setData("Someting wrong with order service");
                responseEntity.setStatus("failed");
                responseEntity.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                return responseEntity;
            }
        } catch (Exception e) {
            responseEntity.setData(e.getMessage());
            responseEntity.setStatus("Someting wrong with order service");
            responseEntity.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            return responseEntity;
        }

    }

}

//System.out.println(request.getUserPrincipal());
////OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) currentUser;
////String userId = oAuth2Authentication.getName();
//SecurityContext context = SecurityContextHolder.getContext();
//String userName = context.getAuthentication().getName();
//
//
//
//
//if (context.getAuthentication().getPrincipal() instanceof KeycloakPrincipal) {
//  KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>)  context.getAuthentication().getPrincipal();
//
//  // this is how to get the real userName (or rather the login name)
//  userName = kp.getKeycloakSecurityContext().getIdToken().getPreferredUsername();
//}
//
////SecurityContext context = SecurityContextHolder.getContext();
////if (context != null && context.getAuthentication() != null) {
////KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) context.getAuthentication();
////System.out.println(authentication.getAccount().getKeycloakSecurityContext());
////}
