package com.tool.bookmyflight.service;

import com.tool.bookmyflight.dto.BookingRequest;
import com.tool.bookmyflight.exception.FlightNotFoundException;
import com.tool.bookmyflight.exception.InsufficientSeatsException;
import com.tool.bookmyflight.model.Booking;
import com.tool.bookmyflight.model.Flight;
import com.tool.bookmyflight.repository.BookingRepository;
import com.tool.bookmyflight.repository.FlightRepository;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;

    public BookingService(FlightRepository flightRepository, BookingRepository bookingRepository) {
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
    }

    public Booking bookFlight(BookingRequest request) {
        // Validate input
        if (request.getNumberOfSeats() <= 0) {
            throw new IllegalArgumentException("Number of seats must be greater than 0");
        }

        // Get flight
        Flight flight = flightRepository.findByFlightNumber(request.getFlightNumber())
            .orElseThrow(() -> new FlightNotFoundException(request.getFlightNumber()));

        // Book seats - thread-safe operation handled by ReentrantReadWriteLock in Flight
        if (!flight.bookSeats(request.getNumberOfSeats())) {
            throw new InsufficientSeatsException(
                request.getFlightNumber(),
                request.getNumberOfSeats(),
                flight.getAvailableSeats()
            );
        }

        // Create booking
        double totalPrice = request.getNumberOfSeats() * flight.getPrice();
        Booking booking = new Booking(
            request.getFlightNumber(),
            request.getPassengerName(),
            request.getEmail(),
            request.getNumberOfSeats(),
            totalPrice
        );

        // Save booking
        return bookingRepository.save(booking);
    }

    public void cancelBooking(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found: " + bookingId);
        }

        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new IllegalArgumentException("Booking already cancelled: " + bookingId);
        }

        // Get flight and release seats
        Flight flight = flightRepository.findByFlightNumber(booking.getFlightNumber())
            .orElseThrow(() -> new FlightNotFoundException(booking.getFlightNumber()));

        // Cancel seats - thread-safe operation handled by ReentrantReadWriteLock in Flight
        flight.cancelSeats(booking.getNumberOfSeats());

        // Update booking status
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    public Flight getFlightInfo(String flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber)
            .orElseThrow(() -> new FlightNotFoundException(flightNumber));
    }
}
