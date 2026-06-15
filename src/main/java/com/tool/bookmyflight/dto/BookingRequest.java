package com.tool.bookmyflight.dto;

public class BookingRequest {
    private String flightNumber;
    private String passengerName;
    private String email;
    private int numberOfSeats;

    public BookingRequest() {}

    public BookingRequest(String flightNumber, String passengerName, String email, int numberOfSeats) {
        this.flightNumber = flightNumber;
        this.passengerName = passengerName;
        this.email = email;
        this.numberOfSeats = numberOfSeats;
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
}

