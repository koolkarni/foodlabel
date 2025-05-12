package com.example.foodlabel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FoodlabelApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodlabelApplication.class, args);
	}

}
