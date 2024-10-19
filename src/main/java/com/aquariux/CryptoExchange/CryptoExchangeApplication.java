package com.aquariux.CryptoExchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.aquariux.CryptoExchange")
@EnableFeignClients(basePackages = "com.aquariux.CryptoExchange")
@EnableJpaRepositories(basePackages = "com.aquariux.CryptoExchange")
@EntityScan(basePackages = "com.aquariux.CryptoExchange")
public class CryptoExchangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoExchangeApplication.class, args);
	}

}
