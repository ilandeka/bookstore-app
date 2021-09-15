package com.royaleleague.service.implementation;

import org.springframework.stereotype.Service;

import com.royaleleague.domain.Payment;
import com.royaleleague.domain.UserPayment;
import com.royaleleague.service.PaymentService;

@Service
public class PaymentServiceImplementation implements PaymentService {

  @Override
  public Payment setByUserPayment(UserPayment userPayment, Payment payment) {
    payment.setType(userPayment.getType());
    payment.setHolderName(userPayment.getHolderName());
    payment.setCardNumber(userPayment.getCardNumber());
    payment.setExpiryMonth(userPayment.getExpiryMonth());
    payment.setExpiryYear(userPayment.getExpiryYear());
    payment.setCvc(userPayment.getCvc());

    return payment;
  }

}
