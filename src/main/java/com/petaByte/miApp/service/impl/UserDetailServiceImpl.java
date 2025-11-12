package com.petaByte.miApp.service.impl;

import com.petaByte.miApp.model.User;
import com.petaByte.miApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Servicio para cargar los detalles de un usuario para Spring Security.
 * Se integra con el AuthenticationManager.
 */
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Carga un usuario por su nombre de usuario O su email.
     * Este método es utilizado por Spring Security durante el proceso de autenticación.
     *
     * @param usernameOrEmail El nombre de usuario o el email proporcionado en el login.
     * @return Un objeto UserDetails que Spring Security utiliza para la autenticación y autorización.
     * @throws UsernameNotFoundException Si no se encuentra ningún usuario con ese username o email.
     */
    @Override
    @Transactional // Es buena práctica para asegurar que los datos lazy (como los roles) se carguen correctamente
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Busca al usuario por su nombre de usuario O por su email
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con: " + usernameOrEmail));

        // Convierte la colección de Roles (de la entidad) a una colección de GrantedAuthority (de Spring Security)
        Collection<? extends GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        // Retorna un objeto UserDetails construido con los datos del usuario encontrado
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername()) // Se usa el username real para el Principal
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}