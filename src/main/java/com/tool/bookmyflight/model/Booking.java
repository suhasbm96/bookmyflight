package com.tool.bookmyflight.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Booking {
    private String bookingId;
    private String flightNumber;
    private String passengerName;
    private String email;
    private int numberOfSeats;
    private double totalPrice;
    private BookingStatus status;
    private LocalDateTime bookingDate;

    public Booking(String flightNumber, String passengerName, String email, int numberOfSeats, double totalPrice) {
        this.bookingId = UUID.randomUUID().toString();
        this.flightNumber = flightNumber;
        this.passengerName = passengerName;
        this.email = email;
        this.numberOfSeats = numberOfSeats;
        this.totalPrice = totalPrice;
        this.status = BookingStatus.CONFIRMED;
        this.bookingDate = LocalDateTime.now();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public enum BookingStatus {
        CONFIRMED, CANCELLED
    }
}

