package com.petaByte.miApp.service.impl;

import com.petaByte.miApp.model.Cart;
import com.petaByte.miApp.model.CartItem;
import com.petaByte.miApp.model.Product;
import com.petaByte.miApp.model.User;
import com.petaByte.miApp.repository.CartItemRepository;
import com.petaByte.miApp.repository.CartRepository;
import com.petaByte.miApp.repository.ProductRepository;
import com.petaByte.miApp.service.CartService;
import com.petaByte.miApp.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional

public class CartServiceImpl implements CartService{
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserService userService; // para obtener user si es necesario

    @Override
    public Cart getCartByUser(User user){
        return cartRepository.findByUser(user)
                .orElseGet(()->{
                    Cart c = Cart.builder().user(user).build();
                    return cartRepository.save(c);
                });
    }

    @Override
    public CartItem addItem(User user, Long productId, int quantity){
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be > 0 ");

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new RuntimeException("Producto no encontrado con id " +productId));

        Cart cart = getCartByUser(user);

        // si existe un item para ese producto, aumentamos cantidad
        Optional<CartItem> existing = cart.getItems().stream()
                .filter(it -> it.getProduct().getId().equals(productId))
                .findFirst();

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
            return item;
        }else {
            CartItem newItem = CartItem.builder()
                    .product(product)
                    .quantity(quantity)
                    .cart(cart)
                    .build();
            CartItem saved = cartItemRepository.save(newItem);
            cart.getItems().add(saved);
            cartRepository.save(cart);
            return saved;
        }
    }

    @Override
    public CartItem updateItemQuantity(User user, Long itemId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be > 0");

        Cart cart = getCartByUser(user);

        CartItem item = cart.getItems().stream()
                .filter(it -> it.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart item no encontrado con id " + itemId));

        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    @Override
    public void removeItem(User user, Long itemId) {
        Cart cart = getCartByUser(user);

        CartItem item = cart.getItems().stream()
                .filter(it -> it.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart item no encontrado con id " + itemId));

        cart.getItems().remove(item);
        cartItemRepository.delete(item);
        cartRepository.save(cart);
    }

    @Override
    public void clearCart(User user) {
        Cart cart = getCartByUser(user);
        cart.getItems().forEach(cartItemRepository::delete);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public void checkout(User user){
        Cart cart = getCartByUser(user);
        // Implementación simple: por ahora solo vaciamos el carrito y podemos crear una Order
        cart.getItems().forEach(cartItemRepository::delete);
        cart.getItems().clear();
        cartRepository.save(cart);

        // TODO: crear Order/ OrderItems y disminuir stock en Product si lo querés
    }




}
