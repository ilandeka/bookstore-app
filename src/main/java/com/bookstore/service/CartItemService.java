package com.bookstore.service;

import java.util.List;

import com.bookstore.domain.Book;
import com.bookstore.domain.CartItem;
import com.bookstore.domain.ShoppingCart;
import com.bookstore.domain.User;

public interface CartItemService {

  List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);

  CartItem updateCartItem(CartItem cartItem);

  CartItem addBookToCardItem(Book book, User user, int qty);

}
