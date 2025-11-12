package com.petaByte.miApp.repository;

import com.petaByte.miApp.model.Role;
import com.petaByte.miApp.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>{
    //Método para buscar un rol por su nombre(ej. "ROLE_USER")
    Optional<Role> findByName(RoleName name);
}
