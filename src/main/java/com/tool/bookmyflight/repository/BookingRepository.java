package com.tool.bookmyflight.repository;

import com.tool.bookmyflight.model.Booking;
import org.springframework.stereotype.Repository;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BookingRepository {
    private final ConcurrentHashMap<String, Booking> bookings = new ConcurrentHashMap<>();

    public Booking save(Booking booking) {
        bookings.put(booking.getBookingId(), booking);
        return booking;
    }

    public Booking findById(String bookingId) {
        return bookings.get(bookingId);
    }

    public List<Booking> findByFlightNumber(String flightNumber) {
        return bookings.values().stream()
            .filter(b -> b.getFlightNumber().equals(flightNumber))
            .collect(Collectors.toList());
    }

    public void delete(String bookingId) {
        bookings.remove(bookingId);
    }
}

