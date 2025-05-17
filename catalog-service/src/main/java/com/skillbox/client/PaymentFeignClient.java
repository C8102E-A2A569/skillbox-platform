package com.skillbox.client;

import com.skillbox.client.dto.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "PaymentFeignClient",
        url = "${payment-service.url}"
)
public interface PaymentFeignClient {

    /**
     * Generate payment link request to payment-service
     *
     * @param request body
     * @param token bearer token
     * @return url for payment
     */
    @PostMapping("/payment/create")
    String generatePaymentLink(@RequestBody PaymentRequest request,
                               @RequestHeader("Authorization") String token);
}
