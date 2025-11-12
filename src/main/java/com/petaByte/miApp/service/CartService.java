package com.petaByte.miApp.service;

import com.petaByte.miApp.model.Cart;
import com.petaByte.miApp.model.CartItem;
import com.petaByte.miApp.model.User;

import java.util.List;

public interface CartService {
Cart getCartByUser(User user);
CartItem addItem(User user, Long productId, int quantity);
CartItem updateItemQuantity(User user, Long itemId, int quantity);
void removeItem(User user, Long itemId);
void clearCart(User user);

//opcional: crear orden y vaciar carrito
    void checkout(User user);
}
