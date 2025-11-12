package com.petaByte.miApp.controller;

import com.petaByte.miApp.model.User;
import com.petaByte.miApp.payload.request.LoginRequest;
import com.petaByte.miApp.payload.request.SignupRequest;
import com.petaByte.miApp.payload.response.JwtResponse;
import com.petaByte.miApp.service.UserService;
import com.petaByte.miApp.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            var userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            var user = userService.getUserByUsername(userDetails.getUsername());

            return ResponseEntity.ok(new JwtResponse(jwt, user.getId(), user.getUsername(), user.getEmail(),
                    user.getRoles().stream().map(r -> r.getName().name()).toList()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Usuario o contraseña incorrectos.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignupRequest signupRequest) {
        try {
            User newUser = new User();
            newUser.setUsername(signupRequest.getUsername());
            newUser.setEmail(signupRequest.getEmail());
            newUser.setPassword(signupRequest.getPassword());
            newUser.setAddress("N/A");
            newUser.setPhoneNumber("N/A");

            User savedUser = userService.registerNewUser(newUser);

            // Login automático
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signupRequest.getUsername(), signupRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            return ResponseEntity.ok(new JwtResponse(jwt, savedUser.getId(), savedUser.getUsername(),
                    savedUser.getEmail(), savedUser.getRoles().stream().map(r -> r.getName().name()).toList()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/perfil")
    public ResponseEntity<?> perfil(Authentication authentication) {
        var userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        var user = userService.getUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(user); // Devuelve todos los datos del usuario
    }
}
