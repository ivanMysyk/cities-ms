package com.test.task.kuehne.cities.handler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { EntityNotFoundException.class })
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Entity not found";
        return handleExceptionInternal(ex, message, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { DataIntegrityViolationException.class })
    protected ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Data integrity violation";
        return handleExceptionInternal(ex, message, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { ConstraintViolationException.class })
    protected ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());
        String message = errors.isEmpty() ? "Invalid request" : String.join("; ", errors);
        return handleExceptionInternal(ex, message, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { MethodArgumentTypeMismatchException.class })
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Invalid request";
        return handleExceptionInternal(ex, message, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
}

//    @ExceptionHandler(value = { MissingServletRequestParameterException.class })
//    protected ResponseEntity<Object> handleMissingServletRequestParameterException(
//            MissingServletRequestParameterException ex, WebRequest request) {
//        String message = ex.getParameterName() + " parameter is missing";
//        return handleExceptionInternal(ex, message, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
//    }

}