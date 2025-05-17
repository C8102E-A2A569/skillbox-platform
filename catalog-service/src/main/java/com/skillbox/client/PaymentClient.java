package com.skillbox.client;

import com.skillbox.client.dto.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Deprecated
@Component
public class PaymentClient {

    @Autowired
    @Qualifier("catalogRestTemplate")
    private RestTemplate restTemplate;

    private static final String PAYMENT_SERVICE_URL = "http://payment-service:8080";

    /**
     * Generates a payment link by calling the payment service
     */
    public String generatePaymentLink(PaymentRequest request) {

        // Call payment service API
        String paymentLink = restTemplate.postForObject(
                PAYMENT_SERVICE_URL + "/payment/create",
                request,
                String.class);
        
        return paymentLink;
    }

    // Inner class for payment request
    private record PaymentLinkRequest(String userId, String courseId, String name, String email, String tariff) {}
}
