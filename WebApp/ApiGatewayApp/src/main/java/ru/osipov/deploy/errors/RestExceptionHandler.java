package ru.osipov.deploy.errors;

import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        ApiError err = new ApiError(error,request.getContextPath(),400,ex.getClass().getName());
        HttpHeaders headers2 = new HttpHeaders();
        headers.set("Content-Type",MediaType.APPLICATION_JSON_UTF8_VALUE);
        return new  ResponseEntity<Object>(err,headers2,HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed parameter: "+ex.getMessage();
        ApiError err = new ApiError(error,request.getContextPath(),400,ex.getClass().getName());
        HttpHeaders headers2 = new HttpHeaders();
        headers.set("Content-Type",MediaType.APPLICATION_JSON_UTF8_VALUE);
        return new  ResponseEntity<Object>(err,headers2,HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Required path parameter: "+ex.getMessage();
        ApiError err = new ApiError(error, request.getContextPath(),400,ex.getClass().getName());
        HttpHeaders headers2 = new HttpHeaders();
        headers.set("Content-Type",MediaType.APPLICATION_JSON_UTF8_VALUE);
        return new  ResponseEntity<Object>(err,headers2,HttpStatus.OK);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Invalid method parameter. BADREQUEST: "+ex.getMessage();
        ApiError err = new ApiError(error, request.getContextPath(),400,ex.getClass().getName());
        HttpHeaders headers2 = new HttpHeaders();
        headers.set("Content-Type",MediaType.APPLICATION_JSON_UTF8_VALUE);
        return new  ResponseEntity<Object>(err,headers2,HttpStatus.OK);
    }

    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<Object> handleApiException(
            ApiException ex) {
        ApiError apiError = new ApiError(ex.getResponseBody(),ex.getPath(),ex.getCode(),null);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type",MediaType.APPLICATION_JSON_UTF8_VALUE);
        return new  ResponseEntity<Object>(apiError,headers,HttpStatus.OK);
    }
}