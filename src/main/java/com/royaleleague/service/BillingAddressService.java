package com.royaleleague.service;

import com.royaleleague.domain.BillingAddress;
import com.royaleleague.domain.UserBilling;

public interface BillingAddressService {

  BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress);

}
