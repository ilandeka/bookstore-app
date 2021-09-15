package com.royaleleague.service;

import com.royaleleague.domain.UserPayment;

public interface UserPaymentService {

  UserPayment findById(Long id);

  void deleteById(Long id);

}
