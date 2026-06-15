package com.tool.bookmyflight.dto;

import com.tool.bookmyflight.model.Booking;

public class BookingResponse {
    private String bookingId;
    private String flightNumber;
    private String passengerName;
    private int numberOfSeats;
    private double totalPrice;
    private String status;

    public BookingResponse(Booking booking) {
        this.bookingId = booking.getBookingId();
        this.flightNumber = booking.getFlightNumber();
        this.passengerName = booking.getPassengerName();
        this.numberOfSeats = booking.getNumberOfSeats();
        this.totalPrice = booking.getTotalPrice();
        this.status = booking.getStatus().toString();
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

