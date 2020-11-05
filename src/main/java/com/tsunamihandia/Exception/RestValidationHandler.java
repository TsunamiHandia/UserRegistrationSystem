package com.tsunamihandia.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class RestValidationHandler {

    // method to handle validation error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<FieldValidationErrorDetails> handleValidationError(
            MethodArgumentNotValidException methodArgumentNotValidException,
            HttpServletRequest request) {

        FieldValidationErrorDetails fieldValidationErrorDetails = new FieldValidationErrorDetails();

        fieldValidationErrorDetails.setError_timeStamp(new Date().getTime());
        fieldValidationErrorDetails.setError_status(HttpStatus.BAD_REQUEST.value());
        fieldValidationErrorDetails.setError_title("Field Validation Error");
        fieldValidationErrorDetails.setError_detail("Inut Field Validation Failed");
        fieldValidationErrorDetails.setError_developer_Message(methodArgumentNotValidException.getClass().getName());
        fieldValidationErrorDetails.setError_path(request.getRequestURI());

        BindingResult result = methodArgumentNotValidException.getBindingResult();
        List<FieldError> fieldErrorList = result.getFieldErrors();

        fieldErrorList.forEach(fieldError -> {
            FieldValidationError fieldValidationError = processFieldError(fieldError);
            List<FieldValidationError> fieldValidationErrorList = fieldValidationErrorDetails.getErrors().get(fieldError.getField());

            if (fieldValidationErrorList == null)
                fieldValidationErrorList = new ArrayList<FieldValidationError>();

            fieldValidationErrorList.add(fieldValidationError);
            fieldValidationErrorDetails.getErrors().put(fieldError.getField(), fieldValidationErrorList);
        });

        return new ResponseEntity<FieldValidationErrorDetails>(fieldValidationErrorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Method to process field error
     * @param fieldError
     * @return
     */
    private FieldValidationError processFieldError(FieldError fieldError) {
        FieldValidationError fieldValidationError = new FieldValidationError();

        if (fieldError != null) {
            fieldValidationError.setFiled(fieldError.getField());
            fieldValidationError.setType(TrayIcon.MessageType.ERROR);
            fieldValidationError.setMessage(fieldError.getDefaultMessage());
        }

        return fieldValidationError;
    }
}
