package com.example.LabSystemBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.bind.annotation.RestController;

//@ServletComponentScan
@SpringBootApplication
@RestController
public class SpringbootTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootTestApplication.class, args);
	}

}

