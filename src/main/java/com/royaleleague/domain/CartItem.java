package com.royaleleague.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CartItem {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private int qty;
  private BigDecimal subTotal;

  @OneToOne
  private Tournament tournament;

  @OneToMany(mappedBy = "cartItem")
  @JsonIgnore
  private List<TournamentToCartItem> tournamentToCartItemList;

  @ManyToOne
  @JoinColumn(name = "shopping_cart_id")
  private ShoppingCart shoppingCart;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getQty() {
    return qty;
  }

  public void setQty(int qty) {
    this.qty = qty;
  }

  public BigDecimal getSubTotal() {
    return subTotal;
  }

  public void setSubTotal(BigDecimal subTotal) {
    this.subTotal = subTotal;
  }

  public Tournament getTournament() {
    return tournament;
  }

  public void setTournament(Tournament tournament) {
    this.tournament = tournament;
  }

  public List<TournamentToCartItem> getTournamentToCartItemList() {
    return tournamentToCartItemList;
  }

  public void setTournamentToCartItemList(List<TournamentToCartItem> tournamentToCartItemList) {
    this.tournamentToCartItemList = tournamentToCartItemList;
  }

  public ShoppingCart getShoppingCart() {
    return shoppingCart;
  }

  public void setShoppingCart(ShoppingCart shoppingCart) {
    this.shoppingCart = shoppingCart;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

}
