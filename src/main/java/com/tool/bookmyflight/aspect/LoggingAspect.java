package com.tool.bookmyflight.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Pointcut for all controller methods in the Flight and Booking controllers
     */
    @Pointcut("execution(* com.tool.bookmyflight.controller.*.*(..))")
    public void controllerMethods() {}

    /**
     * Pointcut for all service methods
     */
    @Pointcut("execution(* com.tool.bookmyflight.service.*.*(..))")
    public void serviceMethods() {}

    /**
     * Around advice to log all controller API calls with request/response details
     */
    @Around("controllerMethods()")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        // Get HTTP request details
        HttpServletRequest request = getHttpRequest();
        String httpMethod = request != null ? request.getMethod() : "UNKNOWN";
        String requestURI = request != null ? request.getRequestURI() : "UNKNOWN";

        // Log request entry at INFO level
        logger.info("==> [{}] API REQUEST: {}.{}() - HTTP {} {}",
            className, className, methodName, httpMethod, requestURI);

        // Log request parameters at DEBUG level
        if (args.length > 0) {
            logger.debug("    Request Parameters: {}", formatParameters(args));
        }

        // Track execution time
        long startTime = System.currentTimeMillis();

        try {
            // Proceed with the actual method execution
            Object result = joinPoint.proceed();

            // Log execution time and response at INFO level
            long executionTime = System.currentTimeMillis() - startTime;
            logger.info("<== [{}] API SUCCESS: {}.{}() - Execution Time: {}ms",
                className, className, methodName, executionTime);

            // Log response at DEBUG level
            logger.debug("    Response: {}", formatResponse(result));

            return result;

        } catch (Exception ex) {
            // Log exception at ERROR level
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("<!> [{}] API ERROR: {}.{}() - Exception after {}ms: {}",
                className, className, methodName, executionTime, ex.getMessage(), ex);
            throw ex;
        }
    }

    /**
     * Around advice to log all service method calls with detailed debugging
     */
    @Around("serviceMethods()")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        // Log service method entry at DEBUG level
        if (logger.isDebugEnabled()) {
            logger.debug(">> [SERVICE] Entering {}.{}() with arguments: {}",
                className, methodName, formatParameters(args));
        }

        long startTime = System.currentTimeMillis();

        try {
            // Proceed with the actual method execution
            Object result = joinPoint.proceed();

            // Log service method exit at DEBUG level
            long executionTime = System.currentTimeMillis() - startTime;
            if (logger.isDebugEnabled()) {
                logger.debug("<< [SERVICE] Exiting {}.{}() - Execution Time: {}ms - Result: {}",
                    className, methodName, executionTime, formatResponse(result));
            }

            return result;

        } catch (Exception ex) {
            // Log service exception at DEBUG level
            long executionTime = System.currentTimeMillis() - startTime;
            logger.debug("xx [SERVICE] Exception in {}.{}() after {}ms: {} - {}",
                className, methodName, executionTime, ex.getClass().getSimpleName(), ex.getMessage());
            throw ex;
        }
    }

    /**
     * Before advice to log when exceptions are thrown
     */
    @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        logger.error("[EXCEPTION] Error in {}.{}: {} - {}",
            className, methodName, exception.getClass().getSimpleName(), exception.getMessage());
    }

    /**
     * Helper method to format method parameters
     */
    private String formatParameters(Object[] args) {
        if (args == null || args.length == 0) {
            return "No parameters";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < args.length; i++) {
            sb.append(formatObject(args[i]));
            if (i < args.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Helper method to safely format response objects
     */
    private String formatResponse(Object response) {
        if (response == null) {
            return "null";
        }

        // Limit response size for large objects
        String responseStr = response.toString();
        if (responseStr.length() > 500) {
            return responseStr.substring(0, 500) + "... [truncated]";
        }
        return responseStr;
    }

    /**
     * Helper method to safely format individual objects
     */
    private String formatObject(Object obj) {
        if (obj == null) {
            return "null";
        }

        String objStr = obj.toString();
        if (objStr.length() > 200) {
            return objStr.substring(0, 200) + "... [truncated]";
        }
        return objStr;
    }

    /**
     * Helper method to get the current HTTP request
     */
    private HttpServletRequest getHttpRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attributes != null ? attributes.getRequest() : null;
        } catch (Exception e) {
            logger.debug("Could not extract HTTP request from context: {}", e.getMessage());
            return null;
        }
    }
}

