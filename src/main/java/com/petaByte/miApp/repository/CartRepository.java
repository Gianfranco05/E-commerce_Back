package com.petaByte.miApp.repository;

import com.petaByte.miApp.model.Cart;
import com.petaByte.miApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional <Cart> findByUser(User user);
}
