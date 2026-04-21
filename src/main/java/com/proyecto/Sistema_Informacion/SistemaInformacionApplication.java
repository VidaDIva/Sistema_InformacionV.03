package com.proyecto.Sistema_Informacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SistemaInformacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaInformacionApplication.class, args);
	}

}
