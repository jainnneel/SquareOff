package com.notification;

import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    
    public void sendEmail(String orderNumber) {
        System.out.println("Order Placed Successfully - Order Number is " + orderNumber);
        System.out.println("Email Sent For Order Id  " + orderNumber);
    }
    
    public void sendEmail(AddPayment orderNumber) {
        System.out.println("Order Placed Successfully - Order Number is " + orderNumber);
        System.out.println("Email Sent For Order Id  " + orderNumber);
    }
}
