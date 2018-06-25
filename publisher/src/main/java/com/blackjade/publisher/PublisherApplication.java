package com.blackjade.publisher;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.blackjade.publisher.dao")
public class PublisherApplication {

	public static void main(String[] args) {
				
		SpringApplication.run(PublisherApplication.class, args);
	}

}




