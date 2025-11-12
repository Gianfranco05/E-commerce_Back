// En: com.petaByte.miApp.config.WebConfig.java

package com.petaByte.miApp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Permite acceder a los archivos en la carpeta 'uploads' a través de la URL '/uploads/**'
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}