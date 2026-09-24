package com.rikkei.b1.dto;

public class BookingData {
    private String bookingId;
    private String userId;
    private String trainId;
    private String seatNo;
    private double ticketPrice;

    public BookingData() {
    }

    public BookingData(String bookingId, String userId, String trainId, String seatNo, double ticketPrice) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.trainId = trainId;
        this.seatNo = seatNo;
        this.ticketPrice = ticketPrice;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
}
