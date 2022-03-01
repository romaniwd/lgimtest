package com.example.lgimtest.errorhandling;

import com.example.lgimtest.LgimtestApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Exception handler for the application.
 */
@ControllerAdvice(basePackageClasses = {LgimtestApplication.class})
@Slf4j
public class LgimTestExceptionHandler {


    @ExceptionHandler(IllegalStateException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(HttpServletRequest httpServletRequest, IllegalStateException e) {

        log.debug("IllegalStateException found", e);
        ErrorResponse errorResponse = new ErrorResponse(e);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleAnyOtherException(HttpServletRequest httpServletRequest, Exception e) {

        log.debug("Exception found", e);
        ErrorResponse errorResponse = new ErrorResponse(e);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
