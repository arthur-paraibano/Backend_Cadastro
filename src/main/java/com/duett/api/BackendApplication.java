package com.duett.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = "com.duett.api")
@ComponentScan(value = {"com.duett.api"})
public class BackendApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
       SpringApplication.run(BackendApplication.class, args);
	}
}
