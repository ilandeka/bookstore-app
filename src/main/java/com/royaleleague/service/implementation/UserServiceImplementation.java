package com.royaleleague.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.royaleleague.domain.ShoppingCart;
import com.royaleleague.domain.User;
import com.royaleleague.domain.UserBilling;
import com.royaleleague.domain.UserPayment;
import com.royaleleague.domain.UserShipping;
import com.royaleleague.domain.security.PasswordResetToken;
import com.royaleleague.domain.security.UserRole;
import com.royaleleague.repository.PasswordResetTokenRepository;
import com.royaleleague.repository.RoleRepository;
import com.royaleleague.repository.UserPaymentRepository;
import com.royaleleague.repository.UserRepository;
import com.royaleleague.repository.UserShippingRepository;
import com.royaleleague.service.UserService;

@Service
public class UserServiceImplementation implements UserService {

  private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordResetTokenRepository passwordResetTokenRepository;

  @Autowired
  private UserPaymentRepository userPaymentRepository;

  @Autowired
  private UserShippingRepository userShippingRepository;

  @Override
  public PasswordResetToken getPasswordResetToken(final String token) {
    return passwordResetTokenRepository.findByToken(token);
  }

  @Override
  public void createPasswordResetTokenForUser(final User user, final String token) {
    final PasswordResetToken myToken = new PasswordResetToken(token, user);
    passwordResetTokenRepository.save(myToken);
  }

  @Override
  public User findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public User findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  @Transactional // Transaction involvement in Database
  public User createUser(User user, Set<UserRole> userRoles) {
    User localUser = userRepository.findByUsername(user.getUsername());

    if (localUser != null) {
      LOG.info("user {} already exists. Try again!", user.getUsername());
    } else {
      for (UserRole userRole : userRoles) {
        roleRepository.save(userRole.getRole());
      }

      user.getUserRoles().addAll(userRoles);

      ShoppingCart shoppingCart = new ShoppingCart();
      shoppingCart.setUser(user);
      user.setShoppingCart(shoppingCart);

      user.setUserShippingList(new ArrayList<UserShipping>());
      user.setUserPaymentList(new ArrayList<UserPayment>());

      localUser = userRepository.save(user);
    }

    return localUser;
  }

  @Override
  public User save(User user) {
    return userRepository.save(user);
  }

  @Override
  public void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user) {
    userPayment.setUser(user);
    userPayment.setUserBilling(userBilling);
    userPayment.setDefaultPayment(true);
    userBilling.setUserPayment(userPayment);
    user.getUserPaymentList().add(userPayment);
    save(user);
  }

  @Override
  public void updateUserShipping(UserShipping userShipping, User user) {
    userShipping.setUser(user);
    userShipping.setUserShippingDefault(true);
    user.getUserShippingList().add(userShipping);
    save(user);
  }

  @Override
  public void setUserDefaultPayment(Long userPaymentId, User user) {
    List<UserPayment> userPaymentList = (List<UserPayment>) userPaymentRepository.findAll();

    for (UserPayment userPayment : userPaymentList) {
      if (userPayment.getId() == userPaymentId) {
        userPayment.setDefaultPayment(true);
        userPaymentRepository.save(userPayment);
      } else {
        userPayment.setDefaultPayment(false);
        userPaymentRepository.save(userPayment);
      }
    }
  }

  @Override
  public void setUserDefaultShipping(Long userShippingId, User user) {
    List<UserShipping> userShippingList = (List<UserShipping>) userShippingRepository.findAll();

    for (UserShipping userShipping : userShippingList) {
      if (userShipping.getId() == userShippingId) {
        userShipping.setUserShippingDefault(true);
        userShippingRepository.save(userShipping);
      } else {
        userShipping.setUserShippingDefault(false);
        userShippingRepository.save(userShipping);
      }
    }
  }

}
