package com.royaleleague.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.royaleleague.domain.TournamentToCartItem;
import com.royaleleague.domain.CartItem;

@Transactional
public interface TournamentToCartItemRepository extends CrudRepository<TournamentToCartItem, Long> {

  void deleteByCartItem(CartItem cartItem);

}
