package com.rikkei.b1.service;

import com.rikkei.b1.event.OrderCreatedEvent;
import com.rikkei.b1.event.PaymentProcessedEvent;
import com.rikkei.b1.event.StockReservedEvent;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SagaChoreographyService {
    private final Map<String, String> orderStatus = new ConcurrentHashMap<>();

    public OrderCreatedEvent createOrder(String orderId, String productId, int quantity, double amount) {
        orderStatus.put(orderId, "CREATED");
        return new OrderCreatedEvent(orderId, productId, quantity, amount);
    }

    public PaymentProcessedEvent processPayment(OrderCreatedEvent event) {
        boolean success = event.getAmount() > 0;
        if (success) {
            orderStatus.put(event.getOrderId(), "PAYMENT_SUCCESS");
        } else {
            orderStatus.put(event.getOrderId(), "PAYMENT_FAILED");
        }
        return new PaymentProcessedEvent(event.getOrderId(), success);
    }

    public StockReservedEvent reserveStock(PaymentProcessedEvent event) {
        if (event.isSuccess()) {
            orderStatus.put(event.getOrderId(), "COMPLETED");
            return new StockReservedEvent(event.getOrderId(), true);
        } else {
            orderStatus.put(event.getOrderId(), "CANCELLED");
            return new StockReservedEvent(event.getOrderId(), false);
        }
    }

    public String getStatus(String orderId) {
        return orderStatus.getOrDefault(orderId, "NOT_FOUND");
    }
}
