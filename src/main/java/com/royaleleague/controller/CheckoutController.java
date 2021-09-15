package com.royaleleague.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.royaleleague.domain.BillingAddress;
import com.royaleleague.domain.CartItem;
import com.royaleleague.domain.Payment;
import com.royaleleague.domain.ShippingAddress;
import com.royaleleague.domain.ShoppingCart;
import com.royaleleague.domain.User;
import com.royaleleague.domain.UserPayment;
import com.royaleleague.domain.UserShipping;
import com.royaleleague.service.BillingAddressService;
import com.royaleleague.service.CartItemService;
import com.royaleleague.service.PaymentService;
import com.royaleleague.service.ShippingAddressService;
import com.royaleleague.service.UserService;
import com.royaleleague.utility.CountriesConstants;

@Controller
public class CheckoutController {

  private final ShippingAddress shippingAddress = new ShippingAddress();
  private final BillingAddress billingAddress = new BillingAddress();
  private final Payment payment = new Payment();

  @Autowired
  private UserService userService;

  @Autowired
  private CartItemService cartItemService;

  @Autowired
  private ShippingAddressService shippingAddressService;

  @Autowired
  private PaymentService paymentService;

  @Autowired
  private BillingAddressService billingAddressService;

  @RequestMapping("/checkout")
  public String checkout(@RequestParam("id") Long cartId, @RequestParam(value = "missingRequiredField", required = false) boolean missingRequiredField, Model model, Principal principal) {
    User user = userService.findByUsername(principal.getName());

    if (cartId != user.getShoppingCart().getId()) {
      return "badRequestPage";
    }

    List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

    if (cartItemList.size() == 0) {
      model.addAttribute("emptyCart", true);
      return "forward:/shoppingCart/cart";
    }

    for (CartItem cartItem : cartItemList) {
      if (cartItem.getTournament().getNumOfRegisteredTeam() < cartItem.getQty()) {
        model.addAttribute("notEnoughStock", true);
        return "forward:/shoppingCart/cart";
      }
    }

    List<UserShipping> userShippingList = user.getUserShippingList();
    List<UserPayment> userPaymentList = user.getUserPaymentList();

    model.addAttribute("userShippingList", userShippingList);
    model.addAttribute("userPaymentList", userPaymentList);

    if (userPaymentList.size() == 0) {
      model.addAttribute("emptyPaymentList", true);
    } else {
      model.addAttribute("emptyPaymentList", false);
    }

    if (userShippingList.size() == 0) {
      model.addAttribute("emptyShippingList", true);
    } else {
      model.addAttribute("emptyShippingList", false);
    }

    ShoppingCart shoppingCart = user.getShoppingCart();

    for (UserShipping userShipping : userShippingList) {
      if (userShipping.isUserShippingDefault()) {
        shippingAddressService.setByUserShipping(userShipping, shippingAddress);
      }
    }

    for (UserPayment userPayment : userPaymentList) {
      if (userPayment.isDefaultPayment()) {
        paymentService.setByUserPayment(userPayment, payment);
        billingAddressService.setByUserBilling(userPayment.getUserBilling(), billingAddress);
      }
    }

    model.addAttribute("shippingAddress", shippingAddress);
    model.addAttribute("payment", payment);
    model.addAttribute("billingAddress", billingAddress);
    model.addAttribute("cartItemList", cartItemList);
    model.addAttribute("shoppingCart", user.getShoppingCart());

    List<String> countriesList = CountriesConstants.listOfCountriesName;
    Collections.sort(countriesList);
    model.addAttribute("countriesList", countriesList);

    model.addAttribute("classActiveShipping", true);

    if (missingRequiredField) {
      model.addAttribute("missingRequiredField", true);
    }

    return "view/checkout";

  }

}
