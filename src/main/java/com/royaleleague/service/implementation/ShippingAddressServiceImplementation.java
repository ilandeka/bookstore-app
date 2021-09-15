package com.royaleleague.service.implementation;

import org.springframework.stereotype.Service;

import com.royaleleague.domain.ShippingAddress;
import com.royaleleague.domain.UserShipping;
import com.royaleleague.service.ShippingAddressService;

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
