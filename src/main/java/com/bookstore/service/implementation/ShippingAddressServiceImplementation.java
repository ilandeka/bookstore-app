package com.bookstore.service.implementation;

import org.springframework.stereotype.Service;

import com.bookstore.domain.ShippingAddress;
import com.bookstore.domain.UserShipping;
import com.bookstore.service.ShippingAddressService;

@Service
public class ShippingAddressServiceImplementation implements ShippingAddressService {

  @Override
  public ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress) {
    shippingAddress.setShippingAddressName(userShipping.getUserShippingName());
    shippingAddress.setShippingAddressStreet1(userShipping.getUserShippingStreet1());
    shippingAddress.setShippingAddressStreet2(userShipping.getUserShippingStreet2());
    shippingAddress.setShippingAddressCity(userShipping.getUserShippingCity());
    shippingAddress.setShippingAddressCountry(userShipping.getUserShippingCountry());
    shippingAddress.setShippingAddressZipCode(userShipping.getUserShippingZipCode());

    return shippingAddress;
  }

}
