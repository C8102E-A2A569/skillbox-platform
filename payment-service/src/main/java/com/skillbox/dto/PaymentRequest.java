package com.skillbox.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private String userId;
    private String courseId;
    private String tariff;
    private String name;
    private String email;
}
