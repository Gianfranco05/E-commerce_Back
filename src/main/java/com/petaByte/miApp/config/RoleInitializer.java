package com.petaByte.miApp.config;

import com.petaByte.miApp.model.Role;
import com.petaByte.miApp.model.RoleName;
import com.petaByte.miApp.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor

public class RoleInitializer {

    // Este método se ejecuta una vez que la aplicación ha iniciado completamente
    @Bean
    public CommandLineRunner initializeRoles(RoleRepository roleRepository){
        return args -> {
            //Verifica si el rol ROLE_USER ya existe
            if (roleRepository.findByName(RoleName.ROLE_USER).isEmpty()){
                Role userRole = new Role();
                userRole.setName(RoleName.ROLE_USER);
                roleRepository.save(userRole);
                System.out.println("Rol ROLE_USER creado exitosamente");
            }

            // Verifica si el rol ROLE_ADMIN ya existe
            if (roleRepository.findByName(RoleName.ROLE_ADMIN).isEmpty()){
                Role adminRole = new Role();
                adminRole.setName(RoleName.ROLE_ADMIN);
                roleRepository.save(adminRole);
                System.out.println("Rol ROLE_ADMIN creado exitosamente.");
            }
        };
    }
}
