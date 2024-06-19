package edu.cscc.securityexercises.handlers;

import edu.cscc.securityexercises.models.ErrorDetail;
import edu.cscc.securityexercises.models.ValidationError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, List<ValidationError>> errors = new HashMap<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        for (FieldError fe : fieldErrors) {
            String field = fe.getField();
            List<ValidationError> validationErrorList = errors.getOrDefault(field, new ArrayList<>());
            ValidationError validationError = new ValidationError(fe.getCode(), fe.getDefaultMessage());
            validationErrorList.add(validationError);
            errors.put(field, validationErrorList);
        }

        ErrorDetail errorDetail = new ErrorDetail("Validation failed", errors);

        return ResponseEntity.badRequest().body(errorDetail);
    }
}
