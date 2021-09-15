package com.royaleleague.service;

import com.royaleleague.domain.Payment;
import com.royaleleague.domain.UserPayment;

public interface PaymentService {

  Payment setByUserPayment(UserPayment userPayment, Payment payment);

}
