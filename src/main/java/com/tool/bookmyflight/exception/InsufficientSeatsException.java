package com.tool.bookmyflight.exception;

public class InsufficientSeatsException extends RuntimeException {
    public InsufficientSeatsException(String flightNumber, int requestedSeats, int availableSeats) {
        super(String.format("Flight %s: Requested %d seats but only %d available",
            flightNumber, requestedSeats, availableSeats));
    }
}

