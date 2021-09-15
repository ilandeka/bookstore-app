package com.royaleleague.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.royaleleague.domain.UserPayment;
import com.royaleleague.repository.UserPaymentRepository;
import com.royaleleague.service.UserPaymentService;

@Service
public class UserPaymentServiceImplementation implements UserPaymentService {

  @Autowired
  UserPaymentRepository userPaymentRepository;

  @Override
  public UserPayment findById(Long id) {
    return userPaymentRepository.findOne(id);
  }

  @Override
  public void deleteById(Long id) {
    userPaymentRepository.delete(id);
  }

}
