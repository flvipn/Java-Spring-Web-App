package important.service;

import important.model.*;
import important.repository.OrderRepository;
import important.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartService cartService;

    @Transactional
    public void createOrder(User user, Order orderDetails) {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty. Cannot proceed with checkout.");
        }

        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> new OrderItem(null, orderDetails, cartItem.getProduct(), cartItem.getQuantity()))
                .collect(Collectors.toList());

        orderDetails.setUser(user);
        orderDetails.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(orderDetails);

        cartItemRepository.deleteByUser(user);
    }
}
