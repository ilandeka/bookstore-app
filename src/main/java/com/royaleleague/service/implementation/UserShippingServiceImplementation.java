package com.royaleleague.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.royaleleague.domain.UserShipping;
import com.royaleleague.repository.UserShippingRepository;
import com.royaleleague.service.UserShippingService;

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
