package com.squareoff;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @RequestMapping(value = "/stockorder")
    public String orderfallback() {
        return "Order Service Fallback";
    }
    
    @RequestMapping(value = "/userservice")
    public String userfallback() {
        return "User Service Fallback";
    }
    
    @RequestMapping(value = "/stockshowing")
    public String stockfallback() {
        return "Stock Service Fallback";
    }
    
    
    
}
