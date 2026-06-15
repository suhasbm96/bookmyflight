package com.tool.bookmyflight.controller;

import com.tool.bookmyflight.dto.FlightResponse;
import com.tool.bookmyflight.model.Flight;
import com.tool.bookmyflight.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "*")
public class FlightController {
    private final BookingService bookingService;

    public FlightController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/{flightNumber}")
    public ResponseEntity<FlightResponse> getFlight(@PathVariable String flightNumber) {
        try {
            Flight flight = bookingService.getFlightInfo(flightNumber);
            return ResponseEntity.ok(new FlightResponse(flight));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

