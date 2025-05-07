package com.skillbox.dto;

import lombok.Data;

@Data
public class PaymentResponse {
    private String paymentLink;
    private String status;
}
