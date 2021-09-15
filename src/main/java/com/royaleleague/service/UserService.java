package com.royaleleague.service;

import java.util.Set;

import com.royaleleague.domain.User;
import com.royaleleague.domain.UserBilling;
import com.royaleleague.domain.UserPayment;
import com.royaleleague.domain.UserShipping;
import com.royaleleague.domain.security.PasswordResetToken;
import com.royaleleague.domain.security.UserRole;

public interface UserService {

  PasswordResetToken getPasswordResetToken(final String token);

  void createPasswordResetTokenForUser(final User user, final String token);

  User findByUsername(String username);

  User findByEmail(String email);

  User createUser(User user, Set<UserRole> userRoles) throws Exception;

  User save(User user);

  void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user);

  void updateUserShipping(UserShipping userShipping, User user);

  void setUserDefaultPayment(Long userPaymentId, User user);

  void setUserDefaultShipping(Long userShippingId, User user);

}
