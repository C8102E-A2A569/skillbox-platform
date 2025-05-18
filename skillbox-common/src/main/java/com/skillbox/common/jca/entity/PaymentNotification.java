package com.skillbox.common.jca.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentNotification {
    private String userId;
    private String paymentId;
    private String email;

}
