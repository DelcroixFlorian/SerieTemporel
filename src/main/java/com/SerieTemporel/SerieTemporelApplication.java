package com.SerieTemporel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath*:application-context.xml"})
@EnableCaching
public class SerieTemporelApplication {

	public static void main(String[] args) {
		SpringApplication.run(SerieTemporelApplication.class, args);
	}

}
