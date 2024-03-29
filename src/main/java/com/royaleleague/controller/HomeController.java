package com.royaleleague.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.royaleleague.domain.Tournament;
import com.royaleleague.domain.User;
import com.royaleleague.domain.UserBilling;
import com.royaleleague.domain.UserPayment;
import com.royaleleague.domain.UserShipping;
import com.royaleleague.domain.security.PasswordResetToken;
import com.royaleleague.domain.security.Role;
import com.royaleleague.domain.security.UserRole;
import com.royaleleague.service.TournamentService;
import com.royaleleague.service.UserPaymentService;
import com.royaleleague.service.UserService;
import com.royaleleague.service.UserShippingService;
import com.royaleleague.service.implementation.UserSecurityService;
import com.royaleleague.utility.CountriesConstants;
import com.royaleleague.utility.MailConstructor;
import com.royaleleague.utility.SecurityUtility;

@Controller
public class HomeController {

  @Autowired
  private JavaMailSender mailSender;

  @Autowired
  private MailConstructor mailConstructor;

  @Autowired
  private UserService userService;

  @Autowired
  private UserSecurityService userSecurityService;

  @Autowired
  private TournamentService tournamentService;

  @Autowired
  private UserPaymentService userPaymentService;

  @Autowired
  private UserShippingService userShippingService;

  @RequestMapping("/")
  public String index() {
    return "index";
  }

  @RequestMapping("/login")
  public String login(Model model) {
    model.addAttribute("classActiveLogin", true);
    return "view/account";
  }

  @RequestMapping("/forgotPassword")
  public String forgotPassword(HttpServletRequest request, @ModelAttribute("email") String userEmail, Model model) {
    model.addAttribute("classActiveForgotPassword", true);

    User user = userService.findByEmail(userEmail);

    if (user == null) {
      model.addAttribute("emailNotExist", true);

      return "view/account";
    }

    String password = SecurityUtility.randomPassword();

    String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
    user.setPassword(encryptedPassword);

    userService.save(user);

    // Universal Unique ID
    String token = UUID.randomUUID().toString();
    userService.createPasswordResetTokenForUser(user, token);

    String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

    SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);

    mailSender.send(newEmail);

    model.addAttribute("forgotPasswordEmailSent", "true");

    return "view/account";
  }

  @RequestMapping(value = "/newUser", method = RequestMethod.POST)
  public String newUserPost(HttpServletRequest request, @ModelAttribute("email") String userEmail, @ModelAttribute("username") String username, Model model) throws Exception {
    model.addAttribute("classActiveNewAccount", true);
    model.addAttribute("email", userEmail);
    model.addAttribute("username", username);

    if (userService.findByUsername(username) != null) {
      model.addAttribute("usernameExists", true);

      return "view/account";
    }

    if (userService.findByEmail(userEmail) != null) {
      model.addAttribute("emailExists", true);

      return "view/account";
    }

    User user = new User();
    user.setUsername(username);
    user.setEmail(userEmail);

    String password = SecurityUtility.randomPassword();

    String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
    user.setPassword(encryptedPassword);

    Role role = new Role();
    role.setRoleId(1);
    role.setName("ROLE_USER");
    Set<UserRole> userRoles = new HashSet<>();
    userRoles.add(new UserRole(user, role));
    userService.createUser(user, userRoles);

    // Universal Unique ID
    String token = UUID.randomUUID().toString();
    userService.createPasswordResetTokenForUser(user, token);

    String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

    SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);

    mailSender.send(email);

    model.addAttribute("emailSent", "true");

    return "view/account";
  }

  @RequestMapping("/newUser")
  public String newUser(Locale locale, @RequestParam("token") String token, Model model) {
    PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);

    // No token
    if (passwordResetToken == null) {
      String message = "Invalid Token";
      model.addAttribute("message", message);
      return "redirect:/badRequest";
    }

    // Retrieve user
    // If user is found by token
    User user = passwordResetToken.getUser();
    String username = user.getUsername();

    // We get user details from user security service
    UserDetails userDetails = userSecurityService.loadUserByUsername(username);

    // Authenticate the user details
    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

    // Make sure the current login in session is for this user
    SecurityContextHolder.getContext().setAuthentication(authentication);

    model.addAttribute("user", user);

    model.addAttribute("classActiveEdit", true);
    return "view/profile";
  }

  @RequestMapping("/tournaments")
  public String tournaments(Model model) {
    List<Tournament> tournamentList = tournamentService.findAll();
    model.addAttribute("tournamentList", tournamentList);
    return "view/tournaments";
  }

  @RequestMapping("/tournamentDetail")
  public String tournamentDetail(@PathParam("id") Long id, Model model, Principal principal) {
    if (principal != null) {
      String username = principal.getName();
      User user = userService.findByUsername(username);
      model.addAttribute("user", user);
    }

    Tournament tournament = tournamentService.findOne(id);
    model.addAttribute("tournament", tournament);

    List<Integer> qtyList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    model.addAttribute("qtyList", qtyList);
    model.addAttribute("qty", 1);

    return "view/tournamentDetail";
  }

  // Principal used to login the user.
  @RequestMapping("/profile")
  public String profile(Model model, Principal principal) {
    User user = userService.findByUsername(principal.getName());
    model.addAttribute("user", user);
    model.addAttribute("userPaymentList", user.getUserPaymentList());
    model.addAttribute("userShippingList", user.getUserShippingList());
    // model.addAttribute("orderList", user.getOrderList());

    UserShipping userShipping = new UserShipping();
    model.addAttribute("userShipping", userShipping);

    model.addAttribute("listOfCreditCards", true);
    model.addAttribute("listOfShippingAddresses", true);

    List<String> countriesList = CountriesConstants.listOfCountriesCode;
    Collections.sort(countriesList);
    model.addAttribute("countriesList", countriesList);
    model.addAttribute("classActiveEdit", true);

    return "view/profile";
  }

  /**
   * Get the list of User's Credit Cards
   */
  @RequestMapping("/listOfCreditCards")
  public String listOfCreditCards(Model model, Principal principal, HttpServletRequest request) {
    User user = userService.findByUsername(principal.getName());
    model.addAttribute("user", user);
    model.addAttribute("userPaymentList", user.getUserPaymentList());
    model.addAttribute("userShippingList", user.getUserShippingList());
    // model.addAttribute("orderList", user.orderList());

    model.addAttribute("listOfCreditCards", true);
    model.addAttribute("classActiveBilling", true);
    model.addAttribute("listOfShippingAddresses", true);

    return "view/profile";
  }

  /**
   * Get the Credit Card of a User
   */
  @RequestMapping("/addNewCreditCard")
  public String addNewCreditCard(Model model, Principal principal) {
    User user = userService.findByUsername(principal.getName());
    model.addAttribute("user", user);

    model.addAttribute("addNewCreditCard", true);
    model.addAttribute("classActiveBilling", true);
    model.addAttribute("listOfShippingAddresses", true);

    UserBilling userBilling = new UserBilling();
    UserPayment userPayment = new UserPayment();

    model.addAttribute("userBilling", userBilling);
    model.addAttribute("userPayment", userPayment);

    List<String> countriesList = CountriesConstants.listOfCountriesName;
    Collections.sort(countriesList);
    model.addAttribute("countriesList", countriesList);
    model.addAttribute("userPaymentList", user.getUserPaymentList());
    model.addAttribute("userShippingList", user.getUserShippingList());
    // model.addAttribute("orderList", user.orderList());

    return "view/profile";
  }

  /**
   * Create the Credit Card for a User
   */
  @RequestMapping(value = "/addNewCreditCard", method = RequestMethod.POST)
  public String addNewCreditCard(@ModelAttribute("userPayment") UserPayment userPayment, @ModelAttribute("userBilling") UserBilling userBilling, Principal principal, Model model) {
    User user = userService.findByUsername(principal.getName());
    userService.updateUserBilling(userBilling, userPayment, user);

    model.addAttribute("user", user);
    model.addAttribute("userPaymentList", user.getUserPaymentList());
    model.addAttribute("userShippingList", user.getUserShippingList());
    model.addAttribute("listOfCreditCards", true);
    model.addAttribute("classActiveBilling", true);
    model.addAttribute("listOfShippingAddresses", true);

    return "view/profile";
  }

  /**
   * Update the Credit Card of a User
   */
  @RequestMapping("/updateCreditCard")
  public String updateCreditCard(@ModelAttribute("id") Long creditCardId, Principal principal, Model model) {
    User user = userService.findByUsername(principal.getName());
    UserPayment userPayment = userPaymentService.findById(creditCardId);

    if (user.getId() != userPayment.getUser().getId()) {
      return "badRequestPage";
    } else {
      model.addAttribute("user", user);
      UserBilling userBilling = userPayment.getUserBilling();
      model.addAttribute("userPayment", userPayment);
      model.addAttribute("userBilling", userBilling);

      List<String> countriesList = CountriesConstants.listOfCountriesName;
      Collections.sort(countriesList);
      model.addAttribute("countriesList", countriesList);

      model.addAttribute("addNewCreditCard", true);
      model.addAttribute("classActiveBilling", true);
      model.addAttribute("listOfShippingAddresses", true);

      model.addAttribute("userPaymentList", user.getUserPaymentList());
      model.addAttribute("userShippingList", user.getUserShippingList());

      return "view/profile";
    }
  }

  /**
   * Delete a Credit Card of a User
   */
  @RequestMapping("/deleteCreditCard")
  public String deleteCreditCard(@ModelAttribute("id") Long creditCardId, Principal principal, Model model) {
    User user = userService.findByUsername(principal.getName());
    UserPayment userPayment = userPaymentService.findById(creditCardId);

    if (user.getId() != userPayment.getUser().getId()) {
      return "badRequestPage";
    } else {
      model.addAttribute("user", user);
      userPaymentService.deleteById(creditCardId);

      model.addAttribute("listOfCreditCards", true);
      model.addAttribute("classActiveBilling", true);
      model.addAttribute("listOfShippingAddresses", true);

      model.addAttribute("userPaymentList", user.getUserPaymentList());
      model.addAttribute("userShippingList", user.getUserShippingList());

      return "view/profile";
    }
  }

  /**
   * Set a default Credit Card for User
   */
  @RequestMapping(value = "/setDefaultPayment", method = RequestMethod.POST)
  public String setDefaultPayment(@ModelAttribute("defaultUserPaymentId") Long defaultPaymentId, Principal principal, Model model) {
    User user = userService.findByUsername(principal.getName());
    userService.setUserDefaultPayment(defaultPaymentId, user);

    model.addAttribute("user", user);
    model.addAttribute("listOfCreditCards", true);
    model.addAttribute("classActiveBilling", true);
    model.addAttribute("listOfShippingAddresses", true);

    model.addAttribute("userPaymentList", user.getUserPaymentList());
    model.addAttribute("userShippingList", user.getUserShippingList());

    return "view/profile";
  }

  /**
   * Get the list of User's Shipping Addresses
   */
  @RequestMapping("/listOfShippingAddresses")
  public String listOfShippingAddresses(Model model, Principal principal, HttpServletRequest request) {
    User user = userService.findByUsername(principal.getName());
    model.addAttribute("user", user);
    model.addAttribute("userPaymentList", user.getUserPaymentList());
    model.addAttribute("userShippingList", user.getUserShippingList());
    // model.addAttribute("orderList", user.orderList());

    model.addAttribute("listOfCreditCards", true);
    model.addAttribute("classActiveShipping", true);
    model.addAttribute("listOfShippingAddresses", true);

    return "view/profile";
  }

  /**
   * Get the Shipping Address of a User
   */
  @RequestMapping("/addNewShippingAddress")
  public String addNewShippingAddress(Model model, Principal principal) {
    User user = userService.findByUsername(principal.getName());
    model.addAttribute("user", user);

    model.addAttribute("addNewShippingAddress", true);
    model.addAttribute("classActiveShipping", true);
    model.addAttribute("listOfCreditCards", true);

    UserShipping userShipping = new UserShipping();
    model.addAttribute("userShipping", userShipping);

    List<String> countriesList = CountriesConstants.listOfCountriesName;
    Collections.sort(countriesList);
    model.addAttribute("countriesList", countriesList);
    model.addAttribute("userPaymentList", user.getUserPaymentList());
    model.addAttribute("userShippingList", user.getUserShippingList());
    // model.addAttribute("orderList", user.orderList());

    return "view/profile";
  }

  /**
   * Create new Shipping Address for User
   */
  @RequestMapping(value = "/addNewShippingAddress", method = RequestMethod.POST)
  public String addNewShippingAddress(@ModelAttribute("userShipping") UserShipping userShipping, Principal principal, Model model) {
    User user = userService.findByUsername(principal.getName());
    userService.updateUserShipping(userShipping, user);

    model.addAttribute("user", user);
    model.addAttribute("userPaymentList", user.getUserPaymentList());
    model.addAttribute("userShippingList", user.getUserShippingList());
    model.addAttribute("listOfShippingAddresses", true);
    model.addAttribute("classActiveShipping", true);
    model.addAttribute("listOfCreditCards", true);

    return "view/profile";
  }

  /**
   * Update the Shipping Address for a User
   */
  @RequestMapping("/updateUserShipping")
  public String updateUserShipping(@ModelAttribute("id") Long shippingAddressId, Principal principal, Model model) {
    User user = userService.findByUsername(principal.getName());
    UserShipping userShipping = userShippingService.findById(shippingAddressId);

    if (user.getId() != userShipping.getUser().getId()) {
      return "badRequestPage";
    } else {
      model.addAttribute("user", user);
      model.addAttribute("userShipping", userShipping);

      List<String> countriesList = CountriesConstants.listOfCountriesName;
      Collections.sort(countriesList);
      model.addAttribute("countriesList", countriesList);

      model.addAttribute("addNewShippingAddress", true);
      model.addAttribute("classActiveShipping", true);
      model.addAttribute("listOfCreditCards", true);

      model.addAttribute("userPaymentList", user.getUserPaymentList());
      model.addAttribute("userShippingList", user.getUserShippingList());

      return "view/profile";
    }
  }

  /**
   * Delete the Shipping Address of a User
   */
  @RequestMapping("/deleteShippingAddress")
  public String deleteShippingAddress(@ModelAttribute("id") Long userShippingId, Principal principal, Model model) {
    User user = userService.findByUsername(principal.getName());
    UserShipping userShipping = userShippingService.findById(userShippingId);

    if (user.getId() != userShipping.getUser().getId()) {
      return "badRequestPage";
    } else {
      model.addAttribute("user", user);

      userShippingService.deleteById(userShippingId);

      model.addAttribute("listOfShippingAddresses", true);
      model.addAttribute("classActiveShipping", true);

      model.addAttribute("userPaymentList", user.getUserPaymentList());
      model.addAttribute("userShippingList", user.getUserShippingList());

      return "view/profile";
    }
  }

  /**
   * Set default Shipping Address for User
   */
  @RequestMapping(value = "/setDefaultShippingAddress", method = RequestMethod.POST)
  public String setDefaultShippingAddress(@ModelAttribute("defaultShippingAddressId") Long defaultShippingId, Principal principal, Model model) {
    User user = userService.findByUsername(principal.getName());
    userService.setUserDefaultShipping(defaultShippingId, user);

    model.addAttribute("user", user);
    model.addAttribute("listOfCreditCards", true);
    model.addAttribute("classActiveShipping", true);
    model.addAttribute("listOfShippingAddresses", true);

    model.addAttribute("userPaymentList", user.getUserPaymentList());
    model.addAttribute("userShippingList", user.getUserShippingList());

    return "view/profile";
  }

}
