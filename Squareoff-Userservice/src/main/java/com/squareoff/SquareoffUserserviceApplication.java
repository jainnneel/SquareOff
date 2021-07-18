package com.squareoff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@RefreshScope
public class SquareoffUserserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SquareoffUserserviceApplication.class, args);
	}

}
