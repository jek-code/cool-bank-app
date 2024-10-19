package com.banking_rest_api.test_demo_bank.exception;

import com.banking_rest_api.test_demo_bank.payload.outgoing.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
    }

    @Test
    void testHandleAccountNotFoundException() {
        // Arrange
        AccountNotFoundException exception = new AccountNotFoundException("Account not found");
        when(webRequest.getDescription(false)).thenReturn("Web request description");

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleAccountNotFoundException(exception, webRequest);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
        assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), errorResponse.getError());
        assertEquals("Account not found", errorResponse.getMessage());
        assertEquals("Web request description", errorResponse.getPath());
    }

    @Test
    void testHandleInsufficientFundsException() {
        // Arrange
        InsufficientFundsException exception = new InsufficientFundsException();
        when(webRequest.getDescription(false)).thenReturn("Web request description");

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInsufficientFundsException(exception, webRequest);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), errorResponse.getStatus());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(), errorResponse.getError());
        assertEquals("Insufficient Funds", errorResponse.getMessage());
        assertEquals("Web request description", errorResponse.getPath());
    }

    @Test
    void testHandleValidationException() {
        // Arrange
        MethodArgumentNotValidException exception = Mockito.mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(Mockito.mock(org.springframework.validation.BindingResult.class));

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationException(exception, webRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorResponse.getError());
    }

    @Test
    void testHandleGlobalException() {
        // Arrange
        Exception exception = new Exception("Generic exception occurred");
        when(webRequest.getDescription(false)).thenReturn("Web request description");

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGlobalException(exception, webRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getStatus());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorResponse.getError());
        assertEquals("Generic exception occurred", errorResponse.getMessage());
        assertEquals("Web request description", errorResponse.getPath());
    }
}
