package com.tool.bookmyflight.repository;

import com.tool.bookmyflight.model.Flight;
import org.springframework.stereotype.Repository;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;

@Repository
public class FlightRepository {
    private final ConcurrentHashMap<String, Flight> flights = new ConcurrentHashMap<>();

    public FlightRepository() {
        // Initialize with sample flights
        initializeSampleFlights();
    }

    private void initializeSampleFlights() {
        flights.put("AA100", new Flight("AA100", "American Airlines", "New York (JFK)", "Los Angeles (LAX)", 150, 299.99));
        flights.put("UA200", new Flight("UA200", "United Airlines", "San Francisco (SFO)", "Chicago (ORD)", 180, 249.99));
        flights.put("DL300", new Flight("DL300", "Delta Airlines", "Atlanta (ATL)", "New York (JFK)", 200, 189.99));
        flights.put("SW400", new Flight("SW400", "Southwest Airlines", "Denver (DEN)", "Las Vegas (LAS)", 160, 129.99));
        flights.put("BA500", new Flight("BA500", "British Airways", "London (LHR)", "New York (JFK)", 250, 599.99));
    }

    public Optional<Flight> findByFlightNumber(String flightNumber) {
        return Optional.ofNullable(flights.get(flightNumber));
    }

    public Flight save(Flight flight) {
        flights.put(flight.getFlightNumber(), flight);
        return flight;
    }

    public void delete(String flightNumber) {
        flights.remove(flightNumber);
    }
}

