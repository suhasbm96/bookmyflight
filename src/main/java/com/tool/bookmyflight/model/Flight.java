package com.tool.bookmyflight.model;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Flight {
    private String flightNumber;
    private String airline;
    private String departure;
    private String arrival;
    private int totalSeats;
    private int availableSeats;
    private double price;

    // ReentrantReadWriteLock for thread-safe read/write operations
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public Flight(String flightNumber, String airline, String departure, String arrival, int totalSeats, double price) {
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.departure = departure;
        this.arrival = arrival;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.price = price;
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

    /**
     * Gets the available seats using read lock.
     * Multiple threads can read available seats simultaneously.
     *
     * @return number of available seats
     */
    public int getAvailableSeats() {
        lock.readLock().lock();
        try {
            return availableSeats;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Sets the available seats using write lock.
     * Only one thread can modify available seats at a time.
     *
     * @param availableSeats number of available seats to set
     */
    public void setAvailableSeats(int availableSeats) {
        lock.writeLock().lock();
        try {
            this.availableSeats = availableSeats;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Atomically books seats if available using write lock.
     * Only one thread can write (book/cancel) at a time.
     * Multiple threads can safely read available seats concurrently.
     *
     * @param numSeats number of seats to book
     * @return true if seats were successfully booked, false if insufficient seats
     */
    public boolean bookSeats(int numSeats) {
        lock.writeLock().lock();
        try {
            if (availableSeats >= numSeats) {
                availableSeats -= numSeats;
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Atomically cancels seats and returns them to available pool using write lock.
     * Only one thread can write (book/cancel) at a time.
     *
     * @param numSeats number of seats to cancel
     */
    public void cancelSeats(int numSeats) {
        lock.writeLock().lock();
        try {
            availableSeats += numSeats;
            if (availableSeats > totalSeats) {
                availableSeats = totalSeats;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}
