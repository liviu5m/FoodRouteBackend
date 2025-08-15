package com.foodroute.foodroute.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentVerificationResponse
{
    private String sessionId;
    private String paymentStatus;
    private Long amountPaid;
    private String currency;
    private String customerEmail;
    private String errorMessage;
}
