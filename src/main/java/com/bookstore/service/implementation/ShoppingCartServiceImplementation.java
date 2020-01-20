package com.bookstore.service.implementation;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.domain.CartItem;
import com.bookstore.domain.ShoppingCart;
import com.bookstore.repository.ShoppingCartRepository;
import com.bookstore.service.CartItemService;
import com.bookstore.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImplementation implements ShoppingCartService {

  @Autowired
  private CartItemService cartItemService;

  @Autowired
  private ShoppingCartRepository shoppingCartRepository;

  @Override
  public ShoppingCart updateShoppingCart(ShoppingCart shoppingCart) {
    BigDecimal cartTotal = new BigDecimal(0);

    List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);

    for (CartItem cartItem : cartItemList) {
      if (cartItem.getBook().getInStockNumber() > 0) {
        cartItemService.updateCartItem(cartItem);
        cartTotal = cartTotal.add(cartItem.getSubTotal());
      }
    }

    shoppingCart.setGrandTotal(cartTotal);

    shoppingCartRepository.save(shoppingCart);

    return shoppingCart;
  }

}
