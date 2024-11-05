package com.example;


import com.example.service.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EntityScan(basePackages = "com.example")
@EnableJpaRepositories(basePackages = "com.example")
public class PaymentsApplication implements CommandLineRunner {

    @Autowired
    private PaymentService paymentService; // Autowire the PaymentService

    public static void main(String[] args) {
        SpringApplication.run(PaymentsApplication.class, args);
    }

    @Override
    public void run(String... args) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("amount", 50000); // Amount in paise
        requestBody.put("currency", "INR");
        requestBody.put("payment_capture", true); // Example parameter

        try {
            // Call the method to process the payment request
            JSONObject response = paymentService.customPaymentRequest(requestBody);
            // Process the response as needed
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

