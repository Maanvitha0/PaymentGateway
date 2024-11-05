package com.example.controller;

import com.example.service.*;
import com.razorpay.Order;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestParam int amount) throws Exception {
        Order order = paymentService.createOrder(amount);
        return ResponseEntity.ok(order.toString());
    }

    @PostMapping("/verify-payment")
    public ResponseEntity<String> verifyPayment(@RequestParam String paymentId, @RequestParam String orderId) {
        try {
            paymentService.validatePayment(paymentId, orderId);
            return ResponseEntity.ok("Payment validated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error validating payment");
        }
    }

    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(@RequestBody Map<String, Object> requestParams) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("amount", requestParams.get("amount")); // Amount in paise
        requestBody.put("currency", "INR");
        requestBody.put("payment_capture", true); // Example parameter

        try {
            JSONObject response = paymentService.customPaymentRequest(requestBody);
            // Process the response as needed
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while creating payment");
        }
    }
}

