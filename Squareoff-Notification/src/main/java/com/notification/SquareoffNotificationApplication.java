package com.notification;

import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

@SpringBootApplication
@EnableEurekaClient
//@RefreshScope
public class SquareoffNotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SquareoffNotificationApplication.class, args);
	}
	
	@Bean
	public Consumer<Message<String>> notificationEventSupplier() {
        return message -> new EmailSender().sendEmail(message.getPayload());
    }
	
//	@Bean
//    public Consumer<Message<OrderSuccess>> notificationEventSupplier1() {
//        return message -> new EmailSender().sendEmail(message.getPayload());
//    }
//	
//	@Bean
//    public Consumer<Message<AddPayment>> notificationEventSupplier2() {
//        return message -> new EmailSender().sendEmail(message.getPayload());
//    }
}
