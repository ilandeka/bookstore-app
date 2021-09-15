package com.royaleleague.service.implementation;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.royaleleague.domain.CartItem;
import com.royaleleague.domain.ShoppingCart;
import com.royaleleague.repository.ShoppingCartRepository;
import com.royaleleague.service.CartItemService;
import com.royaleleague.service.ShoppingCartService;

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
      if (cartItem.getTournament().getNumOfRegisteredTeam() > 0) {
        cartItemService.updateCartItem(cartItem);
        cartTotal = cartTotal.add(cartItem.getSubTotal());
      }
    }

    shoppingCart.setGrandTotal(cartTotal);

    shoppingCartRepository.save(shoppingCart);

    return shoppingCart;
  }

}
