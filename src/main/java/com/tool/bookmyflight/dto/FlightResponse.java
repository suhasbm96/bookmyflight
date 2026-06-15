package com.tool.bookmyflight.dto;

import com.tool.bookmyflight.model.Flight;

public class FlightResponse {
    private String flightNumber;
    private String airline;
    private String departure;
    private String arrival;
    private int totalSeats;
    private int availableSeats;
    private double price;

    public FlightResponse(Flight flight) {
        this.flightNumber = flight.getFlightNumber();
        this.airline = flight.getAirline();
        this.departure = flight.getDeparture();
        this.arrival = flight.getArrival();
        this.totalSeats = flight.getTotalSeats();
        this.availableSeats = flight.getAvailableSeats();
        this.price = flight.getPrice();
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

