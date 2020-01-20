package com.bookstore.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.domain.UserShipping;
import com.bookstore.repository.UserShippingRepository;
import com.bookstore.service.UserShippingService;

@Service
public class UserShippingServiceImplementation implements UserShippingService {

  @Autowired
  private UserShippingRepository userShippingRepository;

  @Override
  public UserShipping findById(Long id) {
    return userShippingRepository.findOne(id);
  }

  @Override
  public void deleteById(Long id) {
    userShippingRepository.delete(id);
  }

}
