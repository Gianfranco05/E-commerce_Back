package com.petaByte.miApp.repository;

import com.petaByte.miApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    //Spring Data JPA lo implementa automáticamente:
    //Necesario para el Login: buscar si existe el usuario
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    //Necesario para el Registro: verificar si ya existe el nombre de usuario
    Boolean existsByUsername (String username);

    //Necesario para el Registro: verificar si ya existe el email
    Boolean existsByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);
}
