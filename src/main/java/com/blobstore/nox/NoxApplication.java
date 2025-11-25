package com.blobstore.nox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class NoxApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoxApplication.class, args);
	}

}
