package com.koss.photocarpet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PhotocarpetApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotocarpetApplication.class, args);
	}

}
