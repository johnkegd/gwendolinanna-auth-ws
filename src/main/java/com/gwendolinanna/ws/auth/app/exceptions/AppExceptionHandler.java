package com.gwendolinanna.ws.auth.app.exceptions;

import com.gwendolinanna.ws.auth.app.ui.model.response.ErrorMessage;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

/**
 * @author Johnkegd
 */
@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = {UserServiceException.class})
    public ResponseEntity<Object> handleUserServiceException(UserServiceException exception, WebRequest request) {
        ErrorMessage customErrorModel = new ErrorMessage(new Date(), exception.getMessage());

        return new ResponseEntity<>(customErrorModel, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Only as example following the spring boot docs and course to have a general custom model for exceptions
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleGeneralExceptions(Exception exception, WebRequest request) {
        ErrorMessage customErrorModel = new ErrorMessage(new Date(),exception.getMessage());
        return new ResponseEntity<>(customErrorModel, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
