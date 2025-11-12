package com.petaByte.miApp.controller;

import com.petaByte.miApp.model.Address;
import com.petaByte.miApp.model.User;
import com.petaByte.miApp.repository.AddressRepository;
import com.petaByte.miApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses") // La URL base será /api/addresses
public class AddressController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    // Endpoint para OBTENER todas las direcciones del usuario logueado
    @GetMapping
    public ResponseEntity<List<Address>> getUserAddresses() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(user.getAddresses());
    }

    // Endpoint para AGREGAR una nueva dirección
    @PostMapping
    public ResponseEntity<?> addAddress(@RequestBody Address newAddress) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificamos el límite de 3 direcciones
        if (user.getAddresses().size() >= 3) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: No se pueden agregar más de 3 direcciones.");
        }

        // Asociamos la dirección con el usuario y la guardamos
        newAddress.setUser(user);
        Address savedAddress = addressRepository.save(newAddress);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAddress);
    }

    // Endpoint para ELIMINAR una dirección por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        // Verificamos que el usuario solo borre sus propias direcciones
        if (!address.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Error: No tenés permiso para borrar esta dirección.");
        }

        addressRepository.delete(address);
        return ResponseEntity.ok("Dirección eliminada correctamente.");
    }
}