package com.royaleleague.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.royaleleague.domain.CartItem;
import com.royaleleague.domain.ShoppingCart;

@Transactional
public interface CartItemRepository extends CrudRepository<CartItem, Long> {

  List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);

}
