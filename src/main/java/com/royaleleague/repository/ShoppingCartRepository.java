package com.royaleleague.repository;

import org.springframework.data.repository.CrudRepository;

import com.royaleleague.domain.ShoppingCart;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long> {

}
