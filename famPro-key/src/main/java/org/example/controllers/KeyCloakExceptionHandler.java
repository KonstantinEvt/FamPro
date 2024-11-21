package org.example.controllers;

import org.example.exceptions.KeyCloakExceptionInfo;
import org.example.exceptions.KeyCloakUserNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class KeyCloakExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<KeyCloakExceptionInfo> handleException(KeyCloakUserNotFound keyCloakUserNotFound) {
        KeyCloakExceptionInfo keyCloakExceptionInfo = new KeyCloakExceptionInfo();
        keyCloakExceptionInfo.setInfo(keyCloakUserNotFound.getMessage());
        return new ResponseEntity<>(keyCloakExceptionInfo, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<KeyCloakExceptionInfo> handleException(Exception exception) {
        KeyCloakExceptionInfo keyCloakExceptionInfo = new KeyCloakExceptionInfo();
        keyCloakExceptionInfo.setInfo(exception.getMessage());
        return new ResponseEntity<>(keyCloakExceptionInfo, HttpStatus.BAD_REQUEST);
    }
}

