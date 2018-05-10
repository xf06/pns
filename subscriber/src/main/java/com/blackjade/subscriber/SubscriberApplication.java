package com.blackjade.subscriber;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.blackjade.subscriber.dao")
public class SubscriberApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubscriberApplication.class, args);
	}
}
