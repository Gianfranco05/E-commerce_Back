package com.petaByte.miApp.controller;

import com.petaByte.miApp.model.Cart;
import com.petaByte.miApp.model.CartItem;
import com.petaByte.miApp.model.User;
import com.petaByte.miApp.payload.request.AddItemRequest;
import com.petaByte.miApp.service.CartService;
import com.petaByte.miApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor

public class CartController {

    private final CartService cartService;
    private final UserService userService;

    // Helper: obtener el usuario autenticado
    private User getAuthenticatedUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userService.getUserByUsername(username);
    }

    @GetMapping
    public ResponseEntity<Cart> getCart(){
        User user = getAuthenticatedUser();
        Cart cart = cartService.getCartByUser(user);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<CartItem> addItem(@RequestBody AddItemRequest request){
        User user = getAuthenticatedUser();
        CartItem added = cartService.addItem(user, request.getProductId(), request.getQuantity());
        return ResponseEntity.status(201).body(added);
    }

    @PutMapping("/update/{itemId}")
    public ResponseEntity<CartItem> updateItem(@PathVariable Long itemId, @RequestBody AddItemRequest request){
        User user = getAuthenticatedUser();
        CartItem updated = cartService.updateItemQuantity(user, itemId, request.getQuantity());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long itemId) {
        User user = getAuthenticatedUser();
        cartService.removeItem(user, itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        User user = getAuthenticatedUser();
        cartService.clearCart(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(){
        User user = getAuthenticatedUser();
        cartService.checkout(user);
        return ResponseEntity.ok("Chekout realizado. Carrito vaciado (aún sin orden persistida");
    }
}
