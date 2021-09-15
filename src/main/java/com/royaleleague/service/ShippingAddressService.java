package com.royaleleague.service;

import com.royaleleague.domain.ShippingAddress;
import com.royaleleague.domain.UserShipping;

public interface ShippingAddressService {

  ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress);

}
