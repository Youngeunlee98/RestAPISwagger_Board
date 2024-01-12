package com.example.mybatisboard.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice  {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorCode> handleCustomException(CustomException e) {
        return new ResponseEntity<>(e.getErrorCode(), HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }




}
