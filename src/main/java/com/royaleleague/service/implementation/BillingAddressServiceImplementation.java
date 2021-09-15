package com.royaleleague.service.implementation;

import org.springframework.stereotype.Service;

import com.royaleleague.domain.BillingAddress;
import com.royaleleague.domain.UserBilling;
import com.royaleleague.service.BillingAddressService;

@Service
public class BillingAddressServiceImplementation implements BillingAddressService {

  @Override
  public BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress) {
    billingAddress.setBillingAddressName(userBilling.getUserBillingName());
    billingAddress.setBillingAddressStreet1(userBilling.getUserBillingStreet1());
    billingAddress.setBillingAddressStreet2(userBilling.getUserBillingStreet2());
    billingAddress.setBillingAddressCity(userBilling.getUserBillingCity());
    billingAddress.setBillingAddressCountry(userBilling.getUserBillingCountry());
    billingAddress.setBillingAddressZipCode(userBilling.getUserBillingZipCode());

    return billingAddress;
  }

}
