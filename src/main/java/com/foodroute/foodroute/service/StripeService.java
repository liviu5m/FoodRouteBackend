package com.foodroute.foodroute.service;

import com.foodroute.foodroute.dto.*;
import com.foodroute.foodroute.model.Order;
import com.foodroute.foodroute.model.OrderStatus;
import com.foodroute.foodroute.model.PaymentStatus;
import com.foodroute.foodroute.model.User;
import com.foodroute.foodroute.repository.OrderRepository;
import com.foodroute.foodroute.repository.ProductRepository;
import com.foodroute.foodroute.repository.UserRepository;
import com.foodroute.foodroute.responses.PaymentVerificationResponse;
import com.foodroute.foodroute.responses.StripeResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String apiKey;

    private final Map<String, PaymentSession> paymentSessions = new ConcurrentHashMap<>();
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public StripeService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
    }

    public StripeResponse createCheckoutSession(ProductRequest productRequestDto) {
        try {
            String verificationToken = UUID.randomUUID().toString();

            User user = userRepository.findById(productRequestDto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
            Order order = new Order();
            order.setStatus(OrderStatus.PENDING);
            order.setUser(user);
            orderRepository.save(order);

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:5173/session/success?session_id={CHECKOUT_SESSION_ID}&token=" + verificationToken + "&orderId="+order.getId())
                    .setCancelUrl("http://localhost:5173/session/cancel?session_id={CHECKOUT_SESSION_ID}&token=" + verificationToken + "&orderId="+order.getId())
                    .addLineItem(createLineItem(productRequestDto))
                    .build();

            Session session = Session.create(params);

            paymentSessions.put(session.getId(),
                    new PaymentSession(session.getId(), verificationToken, PaymentStatus.PENDING));

            return StripeResponse.builder()
                    .status("SUCCESS")
                    .message("Payment session created")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .verificationToken(verificationToken)
                    .build();

        } catch (StripeException e) {
            throw new RuntimeException("Failed to create checkout session", e);
        }
    }

    private SessionCreateParams.LineItem createLineItem(ProductRequest productRequestDto) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(productRequestDto.getQuantity())
                .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(productRequestDto.getCurrency() == null ? "USD" : productRequestDto.getCurrency())
                                .setUnitAmount(productRequestDto.getAmount())
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName(productRequestDto.getName())
                                                .build())
                                .build())
                .build();
    }

    public PaymentVerificationResponse verifyPaymentWithToken(String sessionId, String token) {
        try {
            PaymentSession paymentSession = paymentSessions.get(sessionId);
            if (paymentSession == null || !paymentSession.getVerificationToken().equals(token)) {
                throw new RuntimeException("Invalid verification token");
            }

            if (paymentSession.getStatus() == PaymentStatus.PAID) {
                return buildVerificationResponse(sessionId);
            }

            Session session = Session.retrieve(sessionId);
            if ("paid".equals(session.getPaymentStatus()) && "complete".equals(session.getStatus())) {
                paymentSession.setStatus(PaymentStatus.PAID);
                return buildVerificationResponse(sessionId);
            }

            return buildVerificationResponse(sessionId);
        } catch (StripeException e) {
            throw new RuntimeException("Stripe verification failed", e);
        }
    }

    private PaymentVerificationResponse buildVerificationResponse(String sessionId) throws StripeException {
        Session session = Session.retrieve(sessionId);
        return PaymentVerificationResponse.builder()
                .sessionId(session.getId())
                .paymentStatus(session.getPaymentStatus())
                .amountPaid(session.getAmountTotal())
                .currency(session.getCurrency())
                .customerEmail(session.getCustomerDetails() != null ?
                        session.getCustomerDetails().getEmail() : null)
                .build();
    }
}