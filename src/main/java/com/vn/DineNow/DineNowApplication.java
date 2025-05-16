package com.vn.DineNow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class DineNowApplication {
	public static void main(String[] args) {
		SpringApplication.run(DineNowApplication.class, args);
	}

}
