package com.proyecto.Sistema_Informacion;

import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration

public class ConfigMVC implements WebMvcConfigurer  {

     private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Value("${app.uploads.base-dir}")
    private String rutaBase;
    
    @Value("${app.uploads.carpetas}")
    private String[] carpetas;

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            WebMvcConfigurer.super.addResourceHandlers(registry);
            for (String carpeta : carpetas) {
                registrarHandler(registry, carpeta);
            }
        }

        private void registrarHandler(ResourceHandlerRegistry registry, String carpeta) {

            // Crear la ruta absoluta del sistema de archivos con "file:"
            String rutaAbs = Paths.get(rutaBase, carpeta).toAbsolutePath().toString();
            String resourceLocation = "file:" + rutaAbs + "/";
        
            logger.info("Registrado handler para la carpeta '{}' : {}", carpeta, resourceLocation);
        
            registry.addResourceHandler("/sistema_informacion/uploads/" + carpeta + "/**")
                    .addResourceLocations(resourceLocation);
        }

}
