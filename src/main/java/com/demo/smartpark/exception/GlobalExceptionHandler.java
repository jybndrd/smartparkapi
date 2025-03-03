package com.demo.smartpark.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

import static org.springframework.http.HttpStatus.*;

/**
 * @author jandrada
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> notFoundException(Exception ex, WebRequest request) {
        var response = ErrorResponse.builder()
                .timestamp(new Date())
                .status(NOT_FOUND.value())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .message(ex.getLocalizedMessage())
                .build();

        return handleExceptionInternal(ex, response, new HttpHeaders(), NOT_FOUND, request);
    }

    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<Object> applicationException(ApplicationException ex, WebRequest request) {
        var response = ErrorResponse.builder()
                .timestamp(new Date())
                .status(INTERNAL_SERVER_ERROR.value())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .message(ex.getLocalizedMessage())
                .build();

        return handleExceptionInternal(ex, response, new HttpHeaders(), INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> unhandledException(Exception ex, WebRequest request) {
        var response = ErrorResponse.builder()
                .timestamp(new Date())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .message("Encountered internal server error. Please contact our Support team.")
                .build();

        return handleExceptionInternal(ex, response, new HttpHeaders(), INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = ParkingLotFullException.class)
    public ResponseEntity<Object> parkingLotFullException(Exception ex, WebRequest request) {
        var response = ErrorResponse.builder()
                .timestamp(new Date())
                .status(FORBIDDEN.value())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .message(ex.getLocalizedMessage())
                .build();

        return handleExceptionInternal(ex, response, new HttpHeaders(), FORBIDDEN, request);
    }

    @ExceptionHandler(value = VehicleAlreadyParkedException.class)
    public ResponseEntity<Object> vehicleAlreadyParkedException(Exception ex, WebRequest request) {
        var response = ErrorResponse.builder()
                .timestamp(new Date())
                .status(FORBIDDEN.value())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .message(ex.getLocalizedMessage())
                .build();

        return handleExceptionInternal(ex, response, new HttpHeaders(), FORBIDDEN, request);
    }

    @ExceptionHandler(value = VehicleIsNotParkedException.class)
    public ResponseEntity<Object> vehicleIsNotParkedException(Exception ex, WebRequest request) {
        var response = ErrorResponse.builder()
                .timestamp(new Date())
                .status(NOT_FOUND.value())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .message(ex.getLocalizedMessage())
                .build();

        return handleExceptionInternal(ex, response, new HttpHeaders(), NOT_FOUND, request);
    }

    @ExceptionHandler(value = RecordAlreadyExist.class)
    public ResponseEntity<Object> recordAlreadyExist(Exception ex, WebRequest request) {
        var response = ErrorResponse.builder()
                .timestamp(new Date())
                .status(FORBIDDEN.value())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .message(ex.getLocalizedMessage())
                .build();

        return handleExceptionInternal(ex, response, new HttpHeaders(), FORBIDDEN, request);
    }
}