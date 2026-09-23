package com.rikkei.b1.event;

public class OrderCreatedEvent {
    private String orderId;
    private String productId;
    private int quantity;
    private double amount;

    public OrderCreatedEvent() {}

    public OrderCreatedEvent(String orderId, String productId, int quantity, double amount) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.amount = amount;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}
