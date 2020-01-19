package ru.osipov.deploy.errors;

import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.osipov.deploy.errors.feign.*;
import ru.osipov.deploy.models.ErrorResponse;

import javax.servlet.http.HttpServletRequest;

import java.security.Provider;
import java.util.concurrent.TimeoutException;

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

    @ExceptionHandler(HttpCanNotCreateException.class)
    protected ResponseEntity<Object> handleHttpCannotCreateException(HttpCanNotCreateException exc){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type",MediaType.APPLICATION_JSON_UTF8_VALUE);
        return new ResponseEntity<Object>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), exc.getMessage()).toString(),headers,HttpStatus.OK);
    }

    @ExceptionHandler(TimeoutException.class)
    protected ResponseEntity<Object> handleTimeoutException(TimeoutException exc){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type",MediaType.APPLICATION_JSON_UTF8_VALUE);
        return new ResponseEntity<Object>(new ErrorResponse(HttpStatus.GATEWAY_TIMEOUT.value(), HttpStatus.GATEWAY_TIMEOUT.getReasonPhrase(), exc.getMessage()).toString(),headers,HttpStatus.OK);
    }

    @ExceptionHandler(HttpNotValueOfParameterException.class)
    protected ResponseEntity<Object> handleHttpNotValueOfParameterException(HttpNotValueOfParameterException exc){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type",MediaType.APPLICATION_JSON_UTF8_VALUE);
        return new ResponseEntity<Object>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), exc.getMessage()).toString(),headers,HttpStatus.OK);
    }

    @ExceptionHandler(CustomClientExceptionWrapper.class)
    protected ResponseEntity<Object> handleCustomClientExceptionWrapper(CustomClientExceptionWrapper exc){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type",MediaType.APPLICATION_JSON_UTF8_VALUE);
        return new ResponseEntity<Object>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), exc.getMessage()).toString(),headers,HttpStatus.OK);
    }

    @ExceptionHandler(ClientBadResponseExceptionWrapper.class)
    protected ResponseEntity<Object> handleClientBadResponseExceptionWrapper(ClientBadResponseExceptionWrapper exc){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type",MediaType.APPLICATION_JSON_UTF8_VALUE);
        return new ResponseEntity<Object>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), exc.getMessage()).toString(),headers,HttpStatus.OK);
    }

    @ExceptionHandler(ClientNotFoundExceptionWrapper.class)
    protected ResponseEntity<Object> handleClientNotFoundExceptionWrapper(ClientNotFoundExceptionWrapper exc){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type",MediaType.APPLICATION_JSON_UTF8_VALUE);
        return new ResponseEntity<Object>(new ErrorResponse(NOT_FOUND.value(), NOT_FOUND.getReasonPhrase(), exc.getMessage()).toString(),headers,HttpStatus.OK);
    }

    @ExceptionHandler({AuthenticationException.class, JwtAuthenticationException.class})
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException exc){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type",MediaType.APPLICATION_JSON_UTF8_VALUE);
        return new ResponseEntity<Object>(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), exc.getMessage()).toString(),headers,HttpStatus.OK);
    }

    @ExceptionHandler(ClientAuthenticationExceptionWrapper.class)
    protected ResponseEntity<Object> handleClientAuthenticationExceptionWrapper(ClientAuthenticationExceptionWrapper exc){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type",MediaType.APPLICATION_JSON_UTF8_VALUE);
        return new ResponseEntity<Object>(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), exc.getMessage()).toString(),headers,HttpStatus.OK);
    }

    @ExceptionHandler(ServiceAccessException.class)
    protected ResponseEntity<Object> handleServiceAccessException(ServiceAccessException exc){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type",MediaType.APPLICATION_JSON_UTF8_VALUE);
        return new ResponseEntity<Object>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exc.getMessage()).toString(),headers,HttpStatus.OK);
    }

    @ExceptionHandler(OtherServiceExceptionWrapper.class)
    protected ResponseEntity<Object> handleOtherServiceExceptionWrapper(OtherServiceExceptionWrapper exc){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type",MediaType.APPLICATION_JSON_UTF8_VALUE);
        return new ResponseEntity<Object>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exc.getMessage()).toString(),headers,HttpStatus.OK);
    }
}