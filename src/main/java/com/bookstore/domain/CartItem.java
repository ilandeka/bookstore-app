package com.bookstore.domain;

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
  private Book book;

  @OneToMany(mappedBy = "cartItem")
  @JsonIgnore
  private List<BookToCartItem> bookToCartItem;

  @ManyToOne
  @JoinColumn(name = "shopping_cart_id")
  private ShoppingCart shoppingCart;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

}
