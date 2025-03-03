package important.controller;

import important.model.Order;
import important.model.User;
import important.service.OrderService;
import important.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;


    @GetMapping("/checkout")
    public String checkoutPage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("cartItems", cartService.getCartItems(user));
        model.addAttribute("order", new Order());
        return "checkout";
    }

    @PostMapping("/place")
    public String placeOrder(@AuthenticationPrincipal User user,
                             @ModelAttribute Order order) {
        orderService.createOrder(user, order);
        return "redirect:/home?orderSuccess=true";
    }

    @GetMapping("/confirmation")
    public String orderConfirmation() {
        return "confirmation";
    }
}
