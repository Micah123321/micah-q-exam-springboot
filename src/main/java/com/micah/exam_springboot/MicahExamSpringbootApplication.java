package com.micah.exam_springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author Micah
 */
@SpringBootApplication
@EnableOpenApi
@MapperScan("com.micah.exam_springboot.dao")
public class MicahExamSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicahExamSpringbootApplication.class, args);
	}

}
