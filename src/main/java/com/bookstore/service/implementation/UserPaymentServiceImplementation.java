package com.bookstore.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.domain.UserPayment;
import com.bookstore.repository.UserPaymentRepository;
import com.bookstore.service.UserPaymentService;

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
