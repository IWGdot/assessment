package com.aquariux.CryptoExchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.aquariux.CryptoExchange")
@EnableFeignClients(basePackages = "com.aquariux.CryptoExchange")
@EnableJpaRepositories(basePackages = "com.aquariux.CryptoExchange")
@EntityScan(basePackages = "com.aquariux.CryptoExchange")
@EnableScheduling
@ComponentScan(basePackages = "com.aquariux.CryptoExchange")
public class CryptoExchangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoExchangeApplication.class, args);
	}

}
