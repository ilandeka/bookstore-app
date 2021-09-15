package com.royaleleague.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class TournamentToCartItem {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "tournament_id")
  private Tournament tournament;

  @ManyToOne
  @JoinColumn(name = "cart_item_id")
  private CartItem cartItem;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Tournament getTournament() {
    return tournament;
  }

  public void setTournament(Tournament tournament) {
    this.tournament = tournament;
  }

  public CartItem getCartItem() {
    return cartItem;
  }

  public void setCartItem(CartItem cartItem) {
    this.cartItem = cartItem;
  }

}
