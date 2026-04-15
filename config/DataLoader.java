package com.proyecto.Sistema_Informacion.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.proyecto.Sistema_Informacion.Model.entity.Cargo;
import com.proyecto.Sistema_Informacion.Model.service.CargoService;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(CargoService cargoService) {
        return args -> {

            // si la tabla cargo está vacía
            if (cargoService.listar().isEmpty()) {

                cargoService.guardar(new Cargo(null, "PACIENTE"));
                cargoService.guardar(new Cargo(null, "DOCTOR"));
                cargoService.guardar(new Cargo(null, "ADMIN"));

                System.out.println("Cargos creados automáticamente");

            }

        };
    }
}