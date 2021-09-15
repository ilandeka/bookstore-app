package com.royaleleague.service.implementation;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.royaleleague.domain.TournamentToCartItem;
import com.royaleleague.domain.CartItem;
import com.royaleleague.domain.ShoppingCart;
import com.royaleleague.domain.Tournament;
import com.royaleleague.domain.User;
import com.royaleleague.repository.TournamentToCartItemRepository;
import com.royaleleague.repository.CartItemRepository;
import com.royaleleague.service.CartItemService;

@Service
public class CartItemServiceImplementation implements CartItemService {

  @Autowired
  private CartItemRepository cartItemRepository;

  @Autowired
  private TournamentToCartItemRepository bookToCartItemRepository;

  @Override
  public List<CartItem> findByShoppingCart(ShoppingCart shoppingCart) {
    return cartItemRepository.findByShoppingCart(shoppingCart);
  }

  @Override
  public CartItem updateCartItem(CartItem cartItem) {
    BigDecimal bigDecimal = new BigDecimal(cartItem.getTournament().getEntryFee()).multiply(new BigDecimal(cartItem.getQty()));

    bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
    cartItem.setSubTotal(bigDecimal);

    cartItemRepository.save(cartItem);

    return cartItem;
  }

  @Override
  public CartItem addTournamentToCartItem(Tournament tournament, User user, int qty) {
    List<CartItem> cartItemList = findByShoppingCart(user.getShoppingCart());

    for (CartItem cartItem : cartItemList) {
      if (tournament.getId() == cartItem.getTournament().getId()) {
        cartItem.setQty(cartItem.getQty() + qty);
        cartItem.setSubTotal(new BigDecimal(tournament.getEntryFee()).multiply(new BigDecimal(qty)));
        cartItemRepository.save(cartItem);
        return cartItem;
      }
    }

    CartItem cartItem = new CartItem();
    cartItem.setShoppingCart(user.getShoppingCart());
    cartItem.setTournament(tournament);
    cartItem.setQty(qty);

    cartItem.setSubTotal(new BigDecimal(tournament.getEntryFee()).multiply(new BigDecimal(qty)));
    cartItem = cartItemRepository.save(cartItem);

    TournamentToCartItem bookToCartItem = new TournamentToCartItem();
    bookToCartItem.setTournament(tournament);
    bookToCartItem.setCartItem(cartItem);
    bookToCartItemRepository.save(bookToCartItem);

    return cartItem;
  }

  @Override
  public CartItem findById(Long id) {
    return cartItemRepository.findOne(id);
  }

  @Override
  public void removeCartItem(CartItem cartItem) {
    bookToCartItemRepository.deleteByCartItem(cartItem);
    cartItemRepository.delete(cartItem);
  }

}
