package important.controller;

import important.model.CartItem;
import important.model.Order;
import important.model.User;
import important.service.CartService;
import important.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String showCart(@AuthenticationPrincipal User user, Model model) {
        List<CartItem> cartItems = cartService.getCartItems(user);
        model.addAttribute("cartItems", cartItems);
        return "cart";
    }

    @PostMapping("/add")
    @ResponseBody
    public String addToCart(@AuthenticationPrincipal User user,
                            @RequestParam Integer productId,
                            @RequestParam int quantity) {
        cartService.addToCart(user, productId, quantity);
        return "Product added to cart!";
    }

    @PostMapping("/checkout")
    public String checkout(@AuthenticationPrincipal User user,
                           @RequestParam String fullName,
                           @RequestParam String address,
                           @RequestParam String phoneNumber) {
        if (user == null) {
            return "redirect:/login";
        }

        Order order = new Order();
        order.setFullName(fullName);
        order.setAddress(address);
        order.setPhoneNumber(phoneNumber);

        orderService.createOrder(user, order);
        cartService.clearCart(user);


        return "redirect:/order/confirmation";
    }

}
