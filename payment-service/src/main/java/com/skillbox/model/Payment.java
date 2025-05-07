package com.skillbox.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class Payment {
    @Id
    private String id;
    private String userId;
    private String courseId;
    private String tariff;
    private String name;
    private String email;
    private String paymentLink;
    private String status; // PENDING, SUCCESS, FAILED
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime expiresAt;
}
