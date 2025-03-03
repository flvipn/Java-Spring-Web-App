package important.service;


import important.model.*;
import important.repository.CartItemRepository;
import important.repository.OrderRepository;
import important.repository.ProductRepo;
import important.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepo productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    @Transactional
    public CartItem addToCart(User user, Integer productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (product.getQuantity() < quantity) {
            throw new IllegalStateException("Not enough stock available!");
        }

        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(user, product);

        if (existingCartItem != null) {
            int newQuantity = existingCartItem.getQuantity() + quantity;

            if (newQuantity > product.getQuantity()) {
                throw new IllegalStateException("Not enough stock available!");
            }

            existingCartItem.setQuantity(newQuantity);
            return cartItemRepository.save(existingCartItem);
        }

        CartItem newItem = new CartItem();
        newItem.setUser(user);
        newItem.setProduct(product);
        newItem.setQuantity(quantity);

        return cartItemRepository.save(newItem);
    }

    public void removeFromCart(String cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Transactional
    public void clearCart(User user) {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (!cartItems.isEmpty()) {
            cartItemRepository.deleteAll(cartItems);
            System.out.println("Cart items successfully deleted!");
        } else {
            System.out.println("Cart is already empty!");
        }
    }

    @Transactional
    public Order checkout(User user, String name, String address) {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty. Cannot proceed with checkout.");
        }

        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> new OrderItem(null, null, cartItem.getProduct(), cartItem.getQuantity()))
                .collect(Collectors.toList());

        Order order = new Order();
        order.setUser(user);
        order.setFullName(name);
        order.setAddress(address);
        order.setPhoneNumber("UNKNOWN");
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        cartItemRepository.deleteByUser(user);

        return savedOrder;
    }


}