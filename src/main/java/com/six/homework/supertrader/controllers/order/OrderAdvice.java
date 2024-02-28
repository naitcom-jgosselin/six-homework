package com.six.homework.supertrader.controllers.order;

import com.six.homework.supertrader.controllers.security.SecurityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class OrderAdvice {
    @ResponseBody
    @ExceptionHandler(InvalidOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    InvalidOrderException invalidOrderHandler(InvalidOrderException ex) {
        return ex;
    }

    @ResponseBody
    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    OrderNotFoundException invalidOrderHandler(OrderNotFoundException ex) {
        return ex;
    }
}
