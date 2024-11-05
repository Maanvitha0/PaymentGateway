package com.example.service;


import com.example.model.*;
import com.example.repository.OrderRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Payment;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    
    @Autowired
    private final RazorpayClient razorpayClient;
    @Autowired
    private final OrderRepository orderRepository;

   
    public PaymentService(OrderRepository orderRepository) throws Exception {
        this.razorpayClient = new RazorpayClient("YOUR_KEY_ID", "YOUR_SECRET_KEY");
        this.orderRepository = orderRepository;
    }

    public Order createOrder(int amount) throws Exception {
        JSONObject options = new JSONObject();
        options.put("amount", amount * 100); // Amount in paise
        options.put("currency", "INR");
        options.put("receipt", "txn_" + System.currentTimeMillis());

        Order order = razorpayClient.orders.create(options);

        // Save order details to the database
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setRazorpayOrderId(order.get("id"));
        orderEntity.setAmount(amount);
        orderEntity.setStatus("CREATED");
        orderRepository.save(orderEntity);

        return order;
    }

    public void validatePayment(String paymentId, String orderId) throws Exception {
        OrderEntity order = orderRepository.findByRazorpayOrderId(orderId)
            .orElseThrow(() -> new Exception("Order not found"));
        order.setStatus("PAID"); // Update order status after validation
        orderRepository.save(order);
    }

    public JSONObject customPaymentRequest(JSONObject requestBody) throws Exception {
        try {
            // Make a custom API request to create a payment
            Payment payment = razorpayClient.payments.createUpi(requestBody); // Corrected method call
            return new JSONObject(payment.toString()); // Return the response
        } catch (Exception e) {
            System.err.println("Error during payment request: " + e.getMessage());
            throw e; // Propagate the exception
        }
    }

    public void initiatePayment(int amount) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("amount", amount * 100); // Amount in paise
        requestBody.put("currency", "INR");
        requestBody.put("payment_capture", true); // Example parameter

        try {
            System.out.println("Request Body: " + requestBody.toString());
            JSONObject response = customPaymentRequest(requestBody); // Call customPaymentRequest
            // Process the response as needed
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
