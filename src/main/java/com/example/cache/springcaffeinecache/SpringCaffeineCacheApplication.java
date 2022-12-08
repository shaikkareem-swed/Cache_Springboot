package com.example.cache.springcaffeinecache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringCaffeineCacheApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringCaffeineCacheApplication.class, args);
	}
}
