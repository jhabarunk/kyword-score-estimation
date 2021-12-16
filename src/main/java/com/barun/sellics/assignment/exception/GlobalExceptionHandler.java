package com.barun.sellics.assignment.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

/**
 * @author barun.jha on 09/12/21
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorInfo handleException(Exception e) {
        LOGGER.error("Exception: ", e);
        return new ErrorInfo(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(URISyntaxException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorInfo handleURISyntaxException(URISyntaxException e) {
        LOGGER.error("URISyntaxException: ", e);
        return new ErrorInfo(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorInfo handleConstraintViolationException(ConstraintViolationException e) {
        LOGGER.error("ConstraintViolationException: ", e);
        return new ErrorInfo(e.getMessage().split(":")[1], LocalDateTime.now());
    }
}
