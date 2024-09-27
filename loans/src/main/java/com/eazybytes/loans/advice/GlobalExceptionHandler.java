package com.eazybytes.loans.advice;


import com.eazybytes.loans.exceptions.LoanAlreadyExistsException;
import com.eazybytes.loans.exceptions.ResourceNotFoundException;
import com.eazybytes.loans.exceptions.RuntimeConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException e){
        ApiError apiError=ApiError.builder()
                .message(e.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeConflictException.class)
    public ResponseEntity<ApiError> handleRuntimeConflictException(RuntimeConflictException e){
        ApiError apiError=ApiError.builder()
                .message(e.getMessage())
                .status(HttpStatus.CONFLICT)
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LoanAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleLoanAlreadyExistException(LoanAlreadyExistsException e){
        ApiError apiError=ApiError.builder()
                .message(e.getMessage())
                .status(HttpStatus.CONFLICT)
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleInternalServerError(Exception e){

        ApiError apiError=ApiError.builder()
                .message(e.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleInputValidationErrors(MethodArgumentNotValidException e){

        List<String> errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError invalidInputErrors = ApiError.builder()
                .message("Invalid Input Errors")
                .subErrors(errors)
                .status(HttpStatus.BAD_REQUEST)
                .build();

        return new ResponseEntity<>(invalidInputErrors, HttpStatus.BAD_REQUEST);
    }





}
