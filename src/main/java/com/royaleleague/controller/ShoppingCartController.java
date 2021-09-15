package com.royaleleague.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.royaleleague.domain.CartItem;
import com.royaleleague.domain.ShoppingCart;
import com.royaleleague.domain.Tournament;
import com.royaleleague.domain.User;
import com.royaleleague.service.CartItemService;
import com.royaleleague.service.ShoppingCartService;
import com.royaleleague.service.TournamentService;
import com.royaleleague.service.UserService;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

  @Autowired
  private UserService userService;

  @Autowired
  private CartItemService cartItemService;

  @Autowired
  private ShoppingCartService shoppingCartService;

  @Autowired
  private TournamentService tournamentService;

  @RequestMapping("/cart")
  public String shoppingCart(Model model, Principal principal) {
    User user = userService.findByUsername(principal.getName());
    ShoppingCart shoppingCart = user.getShoppingCart();

    List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);

    /*
     * Update ShoppingCart before in case there is price change or product is unavailable
     */
    shoppingCartService.updateShoppingCart(shoppingCart);

    model.addAttribute("cartItemList", cartItemList);
    model.addAttribute("shoppingCart", shoppingCart);

    return "view/shoppingCart";
  }

  @RequestMapping("/addItem")
  public String addItem(@ModelAttribute("tournament") Tournament tournament, @ModelAttribute("qty") String qty, Model model, Principal principal) {
    User user = userService.findByUsername(principal.getName());
    tournament = tournamentService.findOne(tournament.getId());

    if (Integer.parseInt(qty) > tournament.getNumOfRegisteredTeam()) {
      model.addAttribute("notEnoughStock", true);
      return "forward:/tournamentDetail?id=" + tournament.getId(); // We can also use redirect instead of forward
    }

    CartItem cartItem = cartItemService.addTournamentToCartItem(tournament, user, Integer.parseInt(qty));
    model.addAttribute("addTournamentSuccess", true);

    return "forward:/tournamentDetail?id=" + tournament.getId();
  }

  @RequestMapping("/updateCartItem")
  public String updateShoppingCart(@ModelAttribute("id") Long cartItemId, @ModelAttribute("qty") int qty) {
    CartItem cartItem = cartItemService.findById(cartItemId);
    cartItem.setQty(qty);
    cartItemService.updateCartItem(cartItem);

    return "forward:/shoppingCart/cart";
  }

  @RequestMapping("/removeItem")
  public String removeItem(@RequestParam("id") Long id) {
    cartItemService.removeCartItem(cartItemService.findById(id));

    return "forward:/shoppingCart/cart";
  }

}
