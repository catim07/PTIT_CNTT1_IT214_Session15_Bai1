package com.rikkei.b1.controller;

import com.rikkei.b1.event.OrderCreatedEvent;
import com.rikkei.b1.event.PaymentProcessedEvent;
import com.rikkei.b1.event.StockReservedEvent;
import com.rikkei.b1.service.SagaChoreographyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final SagaChoreographyService sagaService;

    public OrderController(SagaChoreographyService sagaService) {
        this.sagaService = sagaService;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestParam String orderId, @RequestParam String productId, @RequestParam int quantity, @RequestParam double amount) {
        OrderCreatedEvent createdEvent = sagaService.createOrder(orderId, productId, quantity, amount);
        PaymentProcessedEvent paymentEvent = sagaService.processPayment(createdEvent);
        StockReservedEvent stockEvent = sagaService.reserveStock(paymentEvent);
        return ResponseEntity.ok("Order processed with final status: " + sagaService.getStatus(orderId));
    }

    @GetMapping("/{orderId}/status")
    public ResponseEntity<String> getStatus(@PathVariable String orderId) {
        return ResponseEntity.ok(sagaService.getStatus(orderId));
    }
}
