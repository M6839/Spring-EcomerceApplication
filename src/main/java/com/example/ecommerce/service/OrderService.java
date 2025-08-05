package com.example.ecommerce.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.OrderItem;
import com.example.ecommerce.entity.UserEntity;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.UserRepository;

@Service
public class OrderService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private OrderRepository orderRepo;
	
	public Order placeOrder(Long userId, String paymentMethod) {
	    UserEntity user = userRepo.findById(userId).orElseThrow();
	    Cart cart = cartService.getCartByUser(userId);

	    Order order = new Order();
	    order.setUser(user);
	    order.setOrderDate(LocalDateTime.now());
	    order.setStatus("PLACED");
	    order.setPaymentMethod(paymentMethod);

	    List<OrderItem> items = new ArrayList<>();
	    double total = 0;

	    for (CartItem ci : cart.getItems()) {
	        OrderItem item = new OrderItem();
	        item.setProduct(ci.getProduct());
	        item.setQuantity(ci.getQuantity());
	        item.setPrice(ci.getProduct().getPrice());
	        item.setOrder(order);
	        items.add(item);
	        total += ci.getProduct().getPrice() * ci.getQuantity();
	    }

	    order.setItems(items);
	    order.setTotalAmount(total);

	    // MOCK payment success
	    if (paymentMethod.equalsIgnoreCase("COD")) {
	        order.setPaymentStatus("PENDING");
	    } else {
	        order.setPaymentStatus("PAID"); // Simulate success
	    }

	    cart.getItems().clear();
	    return orderRepo.save(order);
	}
	
	public List<Order> getUserOrders(Long userId) {
        UserEntity user = userRepo.findById(userId).orElseThrow();
        return orderRepo.findByUser(user);
    }


}
