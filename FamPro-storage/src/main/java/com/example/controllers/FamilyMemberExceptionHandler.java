package com.example.controllers;

import com.example.exceptions.ProblemWithId;
import com.example.exceptions.FamilyMemberExceptionInfo;
import com.example.exceptions.FamilyMemberNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FamilyMemberExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<FamilyMemberExceptionInfo> handleException(FamilyMemberNotFound familyMemberNotFound) {
        FamilyMemberExceptionInfo familyMemberExceptionInfo = new FamilyMemberExceptionInfo();
        familyMemberExceptionInfo.setInfo(familyMemberNotFound.getMessage());
        return new ResponseEntity<>(familyMemberExceptionInfo, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<FamilyMemberExceptionInfo> handleException(ProblemWithId problemWithId) {
        FamilyMemberExceptionInfo familyMemberException = new FamilyMemberExceptionInfo();
        familyMemberException.setInfo(problemWithId.getMessage());
        return new ResponseEntity<>(familyMemberException, HttpStatus.NOT_ACCEPTABLE);
    }
    @ExceptionHandler
    public ResponseEntity<FamilyMemberExceptionInfo> handleException(Exception exception) {
        FamilyMemberExceptionInfo familyMemberException = new FamilyMemberExceptionInfo();
        familyMemberException.setInfo(exception.getMessage());
        return new ResponseEntity<>(familyMemberException, HttpStatus.BAD_REQUEST);
    }
}

