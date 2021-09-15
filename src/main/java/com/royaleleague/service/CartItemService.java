package com.royaleleague.service;

import java.util.List;

import com.royaleleague.domain.CartItem;
import com.royaleleague.domain.ShoppingCart;
import com.royaleleague.domain.Tournament;
import com.royaleleague.domain.User;

public interface CartItemService {

  List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);

  CartItem updateCartItem(CartItem cartItem);

  CartItem addTournamentToCartItem(Tournament tournament, User user, int qty);

  CartItem findById(Long id);

  void removeCartItem(CartItem cartItem);
}
