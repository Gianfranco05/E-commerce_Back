package com.petaByte.miApp.service;

import com.petaByte.miApp.model.Role;
import com.petaByte.miApp.model.RoleName;
import com.petaByte.miApp.model.User;
import com.petaByte.miApp.repository.RoleRepository;
import com.petaByte.miApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public User registerNewUser(User user) {
        if(userRepository.existsByUsername(user.getUsername())){
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }
        if(userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("El email ya está en uso");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("No se encontró el rol ROLE_USER"));
        roles.add(userRole);
        user.setRoles(roles);

        if(user.getAddress() == null) user.setAddress("N/A");
        if(user.getPhoneNumber() == null) user.setPhoneNumber("N/A");

        return userRepository.save(user);
    }
}
