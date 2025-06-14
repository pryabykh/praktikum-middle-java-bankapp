package com.pryabykh.bankapp.transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CashApplication {

	public static void main(String[] args) {
		SpringApplication.run(CashApplication.class, args);
	}

}
