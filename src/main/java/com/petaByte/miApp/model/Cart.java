package com.petaByte.miApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Carrito asociado a un usuario (1:1)
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("user-cart")
    private User user;

    // Items del carrito
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference("cart-items")
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

    // Helper: Calcular total dinámicamente

    public double getTotal(){
        return items.stream()
                .mapToDouble(it -> it.getQuantity() * (it.getProduct() != null ? it.getProduct().getPrice() : 0.0))
                .sum();
    }
}
