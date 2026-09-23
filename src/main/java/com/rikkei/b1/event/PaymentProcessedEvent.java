package com.rikkei.b1.event;

public class PaymentProcessedEvent {
    private String orderId;
    private boolean success;

    public PaymentProcessedEvent() {}

    public PaymentProcessedEvent(String orderId, boolean success) {
        this.orderId = orderId;
        this.success = success;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}
