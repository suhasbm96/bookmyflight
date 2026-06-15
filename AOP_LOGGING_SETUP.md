# AOP Logging Implementation

## Overview
This document describes the Aspect-Oriented Programming (AOP) logging implementation for the BookMyFlight API service. The logging aspect provides comprehensive INFO and DEBUG level logging for all API controller and service methods.

## Components Added

### 1. **Dependencies (pom.xml)**
Added the following dependencies:
- `spring-boot-starter-aop`: Provides Spring AOP support
- `aspectjweaver`: AspectJ weaver for aspect implementation

### 2. **LoggingAspect Class**
Location: `src/main/java/com/tool/bookmyflight/aspect/LoggingAspect.java`

The aspect provides cross-cutting concerns for logging with the following features:

#### **Logging Pointcuts**

1. **Controller Methods Pointcut**
   - Pattern: `execution(* com.tool.bookmyflight.controller.*.*(..))`
   - Logs all API endpoint calls with request/response details

2. **Service Methods Pointcut**
   - Pattern: `execution(* com.tool.bookmyflight.service.*.*(..))`
   - Logs all service layer operations with detailed execution information

#### **Advice Types**

1. **@Around Advice - Controller Methods**
   - Logs API request entry with HTTP method, URI, and parameters
   - Logs execution time and response
   - Logs exceptions with detailed error information
   - Log Level: **INFO** for entry/exit/success, **ERROR** for exceptions

2. **@Around Advice - Service Methods**
   - Logs service method entry and exit
   - Logs method arguments and return values
   - Logs execution time and exceptions
   - Log Level: **DEBUG** for detailed service operations

3. **@AfterThrowing Advice**
   - Logs when exceptions are thrown from controller methods
   - Captures exception class and message
   - Log Level: **ERROR**

### 3. **Application Properties Configuration**
Updated: `src/main/resources/application.properties`

Logging levels configured:
```properties
logging.level.root=INFO
logging.level.com.tool.bookmyflight=DEBUG
logging.level.com.tool.bookmyflight.aspect=DEBUG
logging.level.com.tool.bookmyflight.controller=INFO
logging.level.com.tool.bookmyflight.service=DEBUG
```

Logging pattern for detailed output:
```properties
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %logger{36} - %msg%n
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

## Logging Output Examples

### API Request/Response Logging (Controller Level)

```
2026-06-15 14:23:45 - com.tool.bookmyflight.aspect.LoggingAspect - ==> [BookingController] API REQUEST: BookingController.createBooking() - HTTP POST /api/bookings
2026-06-15 14:23:45 - com.tool.bookmyflight.aspect.LoggingAspect - Request Parameters: [BookingRequest{flightNumber='AA123', passengerName='John Doe', email='john@example.com', numberOfSeats=2}]
2026-06-15 14:23:45 - com.tool.bookmyflight.aspect.LoggingAspect - >> [SERVICE] Entering BookingService.bookFlight() with arguments: [BookingRequest{...}]
2026-06-15 14:23:46 - com.tool.bookmyflight.aspect.LoggingAspect - << [SERVICE] Exiting BookingService.bookFlight() - Execution Time: 5ms - Result: Booking{...}
2026-06-15 14:23:46 - com.tool.bookmyflight.aspect.LoggingAspect - <== [BookingController] API SUCCESS: BookingController.createBooking() - Execution Time: 10ms
2026-06-15 14:23:46 - com.tool.bookmyflight.aspect.LoggingAspect - Response: ResponseEntity{body=BookingResponse{...}, status=201 CREATED}
```

### Error Handling and Logging

```
2026-06-15 14:25:30 - com.tool.bookmyflight.aspect.LoggingAspect - ==> [BookingController] API REQUEST: BookingController.createBooking() - HTTP POST /api/bookings
2026-06-15 14:25:30 - com.tool.bookmyflight.aspect.LoggingAspect - << [SERVICE] Exception in BookingService.bookFlight() after 2ms: FlightNotFoundException - Flight not found
2026-06-15 14:25:30 - com.tool.bookmyflight.aspect.LoggingAspect - <!> [BookingController] API ERROR: BookingController.createBooking() - Exception after 5ms: Flight not found
2026-06-15 14:25:30 - com.tool.bookmyflight.aspect.LoggingAspect - [EXCEPTION] Error in BookingController.createBooking: FlightNotFoundException - Flight not found
```

## Log Level Details

| Level | Usage | What's Logged |
|-------|-------|--------------|
| **INFO** | Controller/API layer | API endpoint name, HTTP method, URI, execution time, response status, success/failure |
| **DEBUG** | Service layer | Method entry/exit, parameters, return values, execution time, detailed business logic operations |
| **ERROR** | Exceptions | Full exception details when errors occur in API calls or service methods |

## Features

✅ **Thread Safety**: Uses thread-safe logging with proper synchronization
✅ **Performance Optimization**: Conditional logging to avoid unnecessary string building
✅ **Object Truncation**: Large objects are truncated to prevent log flooding (500 chars for responses, 200 chars for parameters)
✅ **HTTP Context Awareness**: Logs HTTP method and URI when available
✅ **Exception Handling**: Gracefully handles requests outside HTTP context
✅ **Execution Time Tracking**: Records the time taken by each method
✅ **Comprehensive Coverage**: Logs synchronously without blocking controller execution

## Integration with Controllers

The aspect automatically intercepts all controller methods:

```java
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest request) {
        // This method is automatically logged by the aspect
        return bookingService.bookFlight(request);
    }
    
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable String bookingId) {
        // This method is automatically logged by the aspect
        return bookingService.cancelBooking(bookingId);
    }
}
```

## IDE Setup

After modifying `pom.xml`, you may need to:

1. **Reload Maven Dependencies** in your IDE:
   - IntelliJ IDEA: Right-click `pom.xml` → Maven → Reload projects
   - VS Code: Command palette → Maven: Reload Maven projects

2. **Invalidate Caches** (IntelliJ IDEA):
   - File → Invalidate Caches → Invalidate and Restart

3. **Build the Project**:
   ```bash
   ./mvnw clean install
   ```

## Testing the Logging

Run the application and make API requests:

```bash
# Start the application
./mvnw spring-boot:run

# In another terminal, make a booking request
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "flightNumber": "AA100",
    "passengerName": "John Doe",
    "email": "john@example.com",
    "numberOfSeats": 2
  }'
```

Check the console output to see the detailed logging information.

## Customization

### Adjust Logging Levels
Modify `application.properties`:

```properties
# Enable more detailed logging
logging.level.com.tool.bookmyflight=TRACE

# Or reduce logging verbosity
logging.level.com.tool.bookmyflight.service=INFO
```

### Extend the Aspect
To add additional logging for other components:

```java
@Pointcut("execution(* com.tool.bookmyflight.repository.*.*(..))")
public void repositoryMethods() {}

@Around("repositoryMethods()")
public Object logRepositoryMethods(ProceedingJoinPoint joinPoint) throws Throwable {
    // Add repository logging logic
}
```

## Performance Consideration

The AOP logging has minimal performance impact because:
- Pointcuts are compiled into bytecode at load time
- Logging is only performed if the log level is enabled
- Large objects are truncated to prevent memory issues
- HTTP context extraction is wrapped in try-catch to handle non-HTTP requests

## Security Note

⚠️ **Important**: The current logging configuration logs request parameters and response body. For production environments with sensitive data (passwords, payment information, etc.), consider:

1. Masking sensitive fields
2. Disabling debug logging for sensitive operations
3. Using a centralized logging service with access controls

Example of adding sensitive data masking:
```java
private String maskSensitiveData(String value) {
    if (value == null || value.length() < 4) {
        return "****";
    }
    return value.substring(0, 4) + "****";
}
```

## Files Modified/Created

| File | Type | Change |
|------|------|--------|
| `pom.xml` | Modified | Added AOP and AspectJ dependencies |
| `src/main/java/com/tool/bookmyflight/aspect/LoggingAspect.java` | Created | Main AOP logging aspect |
| `src/main/resources/application.properties` | Modified | Added logging configuration |

---

**Last Updated**: June 15, 2026
**Version**: 1.0

