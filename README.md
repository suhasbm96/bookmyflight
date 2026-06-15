# BookMyFlight - Flight Ticket Booking REST API

A simple REST API for managing flight ticket bookings with in-memory storage. This service allows users to book flights and check flight availability without the risk of overbooking.

## Features

- **Book flights**: Create reservations for specific flights
- **Cancel bookings**: Cancel existing reservations and release seats
- **Flight information**: Check flight details and seat availability
- **Overbooking prevention**: Thread-safe seat management to prevent double-booking
- **In-memory storage**: Fast, transient data storage (data is lost on restart)
- **Error handling**: Comprehensive exception handling with appropriate HTTP status codes

## Technology Stack

- **Framework**: Spring Boot 4.1.0
- **Language**: Java 17
- **Build Tool**: Maven
- **Database**: H2 In-Memory Database
- **Architecture**: MVC with Service Layer

## Project Structure

```
src/main/java/com/tool/bookmyflight/
├── controller/              # REST API endpoints
│   ├── BookingController.java
│   ├── FlightController.java
│   └── GlobalExceptionHandler.java
├── service/                 # Business logic
│   └── BookingService.java
├── repository/              # Data storage
│   ├── BookingRepository.java
│   └── FlightRepository.java
├── model/                   # Domain entities
│   ├── Booking.java
│   └── Flight.java
├── dto/                     # Request/Response objects
│   ├── BookingRequest.java
│   ├── BookingResponse.java
│   └── FlightResponse.java
├── exception/               # Custom exceptions
│   ├── FlightNotFoundException.java
│   └── InsufficientSeatsException.java
└── BookmyflightApplication.java
```

## How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Build the application
```bash
cd /Users/suhas/Downloads/bookmyflight
./mvnw clean package
```

### Run the application
```bash
./mvnw spring-boot:run
```

The server will start on `http://localhost:8080`

### Build and run with Docker (optional)
```bash
./mvnw clean package
docker build -t bookmyflight .
docker run -p 8080:8080 bookmyflight
```

## API Endpoints

### 1. Get Flight Information
**Endpoint**: `GET /api/flights/{flightNumber}`

**Description**: Retrieve details and availability of a specific flight

**Response** (200 OK):
```json
{
  "flightNumber": "AA100",
  "airline": "American Airlines",
  "departure": "New York (JFK)",
  "arrival": "Los Angeles (LAX)",
  "totalSeats": 150,
  "availableSeats": 135,
  "price": 299.99
}
```

**Error Response** (404 NOT FOUND):
```json
{
  "error": "FLIGHT_NOT_FOUND",
  "message": "Flight not found: XX999"
}
```

---

### 2. Create a Booking
**Endpoint**: `POST /api/bookings`

**Description**: Book seats on a flight for a passenger

**Request Body**:
```json
{
  "flightNumber": "AA100",
  "passengerName": "John Doe",
  "email": "john.doe@example.com",
  "numberOfSeats": 2
}
```

**Response** (201 CREATED):
```json
{
  "bookingId": "a3f9c2e1-5b4d-4f6a-9e8c-1b2a3d4c5e6f",
  "flightNumber": "AA100",
  "passengerName": "John Doe",
  "numberOfSeats": 2,
  "totalPrice": 599.98,
  "status": "CONFIRMED"
}
```

**Error Responses**:

- **400 BAD REQUEST** - Invalid number of seats or missing fields:
```json
{
  "error": "INVALID_REQUEST",
  "message": "Number of seats must be greater than 0"
}
```

- **404 NOT FOUND** - Flight does not exist:
```json
{
  "error": "FLIGHT_NOT_FOUND",
  "message": "Flight not found: XX999"
}
```

- **409 CONFLICT** - Not enough seats available:
```json
{
  "error": "INSUFFICIENT_SEATS",
  "message": "Flight AA100: Requested 100 seats but only 50 available"
}
```

---

### 3. Cancel a Booking
**Endpoint**: `DELETE /api/bookings/{bookingId}`

**Description**: Cancel an existing booking and release the seats back to the flight

**Response** (204 NO CONTENT): No response body

**Error Response** (404 NOT FOUND):
```json
{
  "error": "INVALID_REQUEST",
  "message": "Booking not found or already cancelled"
}
```

---

## Sample Requests

### Using cURL

#### Get Flight Information
```bash
curl -X GET http://localhost:8080/api/flights/AA100 \
  -H "Content-Type: application/json"
```

#### Create a Booking
```bash
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "flightNumber": "AA100",
    "passengerName": "John Doe",
    "email": "john.doe@example.com",
    "numberOfSeats": 2
  }'
```

#### Cancel a Booking
```bash
curl -X DELETE http://localhost:8080/api/bookings/a3f9c2e1-5b4d-4f6a-9e8c-1b2a3d4c5e6f \
  -H "Content-Type: application/json"
```

### Using HTTP Client (IntelliJ IDEA)

Create a file `requests.http` in your project:
```http
### Get Flight Details
GET http://localhost:8080/api/flights/AA100
Content-Type: application/json

### Create a Booking
POST http://localhost:8080/api/bookings
Content-Type: application/json

{
  "flightNumber": "AA100",
  "passengerName": "John Doe",
  "email": "john.doe@example.com",
  "numberOfSeats": 2
}

### Cancel a Booking
DELETE http://localhost:8080/api/bookings/{bookingId}
Content-Type: application/json
```

### Using Postman

1. **Import Collection**: Create a new Postman collection
2. **Add Requests**:
   - GET `http://localhost:8080/api/flights/AA100`
   - POST `http://localhost:8080/api/bookings` with the booking request body
   - DELETE `http://localhost:8080/api/bookings/{bookingId}`

## Available Flights

The system comes pre-loaded with the following flights:

| Flight Number | Airline | Departure | Arrival | Seats | Price |
|---|---|---|---|---|---|
| AA100 | American Airlines | New York (JFK) | Los Angeles (LAX) | 150 | $299.99 |
| UA200 | United Airlines | San Francisco (SFO) | Chicago (ORD) | 180 | $249.99 |
| DL300 | Delta Airlines | Atlanta (ATL) | New York (JFK) | 200 | $189.99 |
| SW400 | Southwest Airlines | Denver (DEN) | Las Vegas (LAS) | 160 | $129.99 |
| BA500 | British Airways | London (LHR) | New York (JFK) | 250 | $599.99 |

## Testing

Run the tests using:
```bash
./mvnw test
```

## Concurrency & Thread Safety

The system uses `ConcurrentHashMap` for data storage and `synchronized` blocks on flight objects during seat booking to prevent race conditions and overbooking. The seat booking operation is atomic, ensuring data consistency.

## Improvements for Production

If given more time, here are the improvements I would implement:

### 1. **Database Integration**
   - Use persistent database (PostgreSQL, MySQL) instead of in-memory storage
   - Data survives application restarts
   - Better data integrity with ACID transactions

### 2. **Authentication & Authorization**
   - Implement JWT-based authentication
   - Role-based access control (admin, customer, agent)
   - Secure endpoints with proper authorization

### 3. **Booking Retrieval & Management**
   - Add GET endpoints to retrieve booking history
   - Get all bookings for a passenger
   - Get booking details by booking ID with confirmation number

### 4. **Enhanced Flight Management**
   - Admin API to add/update flights
   - Dynamic pricing based on availability
   - Flight search by route, date, airline

### 5. **Seat Selection**
   - Specific seat selection instead of just number of seats
   - Seat map visualization
   - Seat class support (economy, business, first)

### 6. **Payment Integration**
   - Payment gateway integration (Stripe, PayPal)
   - Payment status tracking
   - Refund management

### 7. **Email Notifications**
   - Booking confirmation emails
   - Cancellation acknowledgment emails
   - Pre-flight reminders

### 8. **API Documentation**
   - Swagger/OpenAPI integration for interactive API docs
   - Better API versioning (v1, v2)

### 9. **Caching**
   - Redis caching for frequently accessed flight data
   - Reduce database queries

### 10. **Comprehensive Testing**
   - Unit tests for service layer
   - Integration tests for API endpoints
   - Load testing for concurrent bookings

### 11. **Logging & Monitoring**
   - Structured logging with ELK stack
   - Application performance monitoring (APM)
   - Error tracking with Sentry

### 12. **Validation & Input Sanitization**
   - Email validation
   - Passenger name validation
   - Request body schema validation with annotations

### 13. **Rate Limiting**
   - API rate limiting to prevent abuse
   - Per-user request throttling

### 14. **Booking Expiration**
   - Temporary booking holds that expire after a certain time
   - Automatic seat release if payment not completed

### 15. **Cancellation Policies**
   - Different refund rules based on cancellation timing
   - Automatic refund processing with payment gateway

## License

This project is open source and available under the MIT License.

## Support

For issues or questions, please contact support or create an issue in the repository.

