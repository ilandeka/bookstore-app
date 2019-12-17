package com.bookstore.controller;

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

import com.bookstore.domain.Book;
import com.bookstore.domain.User;
import com.bookstore.domain.UserBilling;
import com.bookstore.domain.UserPayment;
import com.bookstore.domain.UserShipping;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.Role;
import com.bookstore.domain.security.UserRole;
import com.bookstore.service.BookService;
import com.bookstore.service.UserService;
import com.bookstore.service.implementation.UserSecurityService;
import com.bookstore.utility.CountriesConstants;
import com.bookstore.utility.MailConstructor;
import com.bookstore.utility.SecurityUtility;

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
  private BookService bookService;

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

  @RequestMapping("/bookshelf")
  public String bookshelf(Model model) {
    List<Book> bookList = bookService.findAll();
    model.addAttribute("bookList", bookList);
    return "view/bookshelf";
  }

  @RequestMapping("/bookDetail")
  public String bookDetail(@PathParam("id") Long id, Model model, Principal principal) {
    if (principal != null) {
      String username = principal.getName();
      User user = userService.findByUsername(username);
      model.addAttribute("user", user);
    }

    Book book = bookService.findOne(id);
    model.addAttribute("book", book);

    List<Integer> qtyList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    model.addAttribute("qtyList", qtyList);
    model.addAttribute("qty", 1);

    return "view/bookDetail";
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

  @RequestMapping("/listOfShippingAddresses")
  public String listOfShippingAddresses(Model model, Principal principal, HttpServletRequest request) {
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

  @RequestMapping("addNewCreditCard")
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

  @RequestMapping("/addNewShippingAddress")
  public String addNewShippingAddress(Model model, Principal principal) {
    User user = userService.findByUsername(principal.getName());
    model.addAttribute("user", user);

    model.addAttribute("addNewShippingAddress", true);
    model.addAttribute("classActiveShipping", true);
    model.addAttribute("listOfCreditCards", true);

    UserShipping userShipping = new UserShipping();
    model.addAttribute("userShipping", userShipping);

    List<String> countriesList = CountriesConstants.listOfCountriesCode;
    Collections.sort(countriesList);
    model.addAttribute("countriesList", countriesList);
    model.addAttribute("userPaymentList", user.getUserPaymentList());
    model.addAttribute("userShippingList", user.getUserShippingList());
    // model.addAttribute("orderList", user.orderList());

    return "view/profile";
  }

}
