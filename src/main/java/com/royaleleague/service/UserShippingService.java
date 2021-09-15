package com.royaleleague.service;

import com.royaleleague.domain.UserShipping;

public interface UserShippingService {

  UserShipping findById(Long id);

  void deleteById(Long id);

}
