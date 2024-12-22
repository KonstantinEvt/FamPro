package org.example.conrollers;

import com.example.dtos.Directive;
import com.example.exceptions.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedList;

@ControllerAdvice
@AllArgsConstructor
public class FamilyMemberExceptionHandler {
    private LinkedList<Directive> directives;
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
    public ResponseEntity<String> handleException(Exception exception) {
        FamilyMemberExceptionInfo familyMemberException = new FamilyMemberExceptionInfo();
        String[] ex=exception.getMessage().split(":");
        String last=ex[ex.length-1].replace("\\","")
                .replace(":","")
                .replace("\"","")
                .replace("]","")
                .replace("}","");
        familyMemberException.setInfo(last);
        return new ResponseEntity<>(familyMemberException.getInfo(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<FamilyMemberExceptionInfo> handleException(Dublicate dublicate) {
        FamilyMemberExceptionInfo familyMemberException = new FamilyMemberExceptionInfo();
        familyMemberException.setInfo(dublicate.getMessage());
        return new ResponseEntity<>(familyMemberException, HttpStatus.CONFLICT);
    }
    @ExceptionHandler
    public ResponseEntity<FamilyMemberExceptionInfo> handleException(UncorrectedInformation uncorrectedInformation) {
        FamilyMemberExceptionInfo familyMemberException = new FamilyMemberExceptionInfo();
        familyMemberException.setInfo(uncorrectedInformation.getMessage());
        return new ResponseEntity<>(familyMemberException, HttpStatus.CONFLICT);
    }
    @ExceptionHandler
    public ResponseEntity<FamilyMemberExceptionInfo> handleException(UncorrectedOrNewInformation uncorrectedOrNewInformation) {
        FamilyMemberExceptionInfo familyMemberException = new FamilyMemberExceptionInfo();
        familyMemberException.setInfo(uncorrectedOrNewInformation.getMessage());
        return new ResponseEntity<>(familyMemberException, HttpStatus.CONFLICT);
    }
}

