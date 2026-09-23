package com.rikkei.b1.event;

public class StockReservedEvent {
    private String orderId;
    private boolean reserved;

    public StockReservedEvent() {}

    public StockReservedEvent(String orderId, boolean reserved) {
        this.orderId = orderId;
        this.reserved = reserved;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public boolean isReserved() { return reserved; }
    public void setReserved(boolean reserved) { this.reserved = reserved; }
}
