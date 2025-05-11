package com.vn.DineNow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DineNowApplication {
	public static void main(String[] args) {
		SpringApplication.run(DineNowApplication.class, args);
	}

}
