package com.cognizant.purchase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableCaching
public class PurchaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(PurchaseApplication.class, args);
	}
	
	@Bean
	   public RestTemplate getRestTemplate() {
	      return new RestTemplate();
	   }

}
