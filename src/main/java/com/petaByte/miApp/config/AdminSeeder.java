package com.petaByte.miApp.config;

import com.petaByte.miApp.model.Role;
import com.petaByte.miApp.model.RoleName;
import com.petaByte.miApp.model.User;
import com.petaByte.miApp.repository.RoleRepository;
import com.petaByte.miApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class AdminSeeder {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner createAdminUser() {
        return args -> {
            // Verificar y crear roles si no existen
            createRoleIfNotExists(RoleName.ROLE_ADMIN);
            createRoleIfNotExists(RoleName.ROLE_USER);

            // Crear usuario admin original si no existe
            if (userRepository.findByEmail("admin2025@petabyte.com").isEmpty()) {
                Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("ROLE_ADMIN no existe."));

                User admin = new User();
                admin.setUsername("Administrador");
                admin.setEmail("admin2025@petabyte.com");
                admin.setPassword(passwordEncoder.encode("admin_123"));
                admin.setAddress("---------");
                admin.setPhoneNumber("0000000000");
                admin.setRoles(Collections.singleton(adminRole));

                userRepository.save(admin);
                System.out.println("✅ Usuario administrador creado: admin2025@petabyte.com / admin_123");
            }

            // ▼▼▼ NUEVO USUARIO ADMINISTRADOR ▼▼▼
            // Crear un segundo admin para pruebas si no existe
            if (userRepository.findByEmail("admin_dev@petabyte.com").isEmpty()) {
                Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("ROLE_ADMIN no existe."));

                User adminDev = new User();
                adminDev.setUsername("admin_dev");
                adminDev.setEmail("admin_dev@petabyte.com");
                adminDev.setPassword(passwordEncoder.encode("dev123")); // Contraseña simple
                adminDev.setAddress("---------12");
                adminDev.setPhoneNumber("0000000012");
                adminDev.setRoles(Collections.singleton(adminRole));

                userRepository.save(adminDev);
                System.out.println("✅ Segundo usuario admin creado: admin_dev / dev123");
            }
        };
    }

    private void createRoleIfNotExists(RoleName roleName) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
            System.out.println("🔹 Rol creado: " + roleName);
        }
    }
}