package com.skillbox.common.jms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOperationMessage {

    private String userId;
    private String courseId;
}
