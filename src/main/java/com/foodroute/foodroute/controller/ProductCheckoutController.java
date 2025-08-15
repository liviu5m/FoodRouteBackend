package com.foodroute.foodroute.controller;

import com.foodroute.foodroute.responses.PaymentVerificationResponse;
import com.foodroute.foodroute.dto.ProductRequest;
import com.foodroute.foodroute.responses.StripeResponse;
import com.foodroute.foodroute.service.StripeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class ProductCheckoutController {
    private StripeService stripeService;

    public ProductCheckoutController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<StripeResponse> checkout(@RequestBody ProductRequest productRequestDto) {
        StripeResponse stripeResponse = stripeService.createCheckoutSession(productRequestDto);
        return ResponseEntity.ok(stripeResponse);
    }

    @GetMapping("/verify")
    public ResponseEntity<PaymentVerificationResponse> verifyPayment(
            @RequestParam String session_id,
            @RequestParam String token) {
        try {
            PaymentVerificationResponse response =
                    stripeService.verifyPaymentWithToken(session_id, token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    PaymentVerificationResponse.builder()
                            .paymentStatus("failed")
                            .errorMessage(e.getMessage())
                            .build());
        }
    }
}
