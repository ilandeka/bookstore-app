package com.royaleleague.repository;

import org.springframework.data.repository.CrudRepository;

import com.royaleleague.domain.UserPayment;

public interface UserPaymentRepository extends CrudRepository<UserPayment, Long> {

}
