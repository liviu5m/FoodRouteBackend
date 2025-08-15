package com.foodroute.foodroute.dto;

import com.foodroute.foodroute.model.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentSession {
    private final String sessionId;
    private final String verificationToken;
    private PaymentStatus status;

    public PaymentSession(String sessionId, String verificationToken, PaymentStatus status) {
        this.sessionId = sessionId;
        this.verificationToken = verificationToken;
        this.status = status;
    }
}