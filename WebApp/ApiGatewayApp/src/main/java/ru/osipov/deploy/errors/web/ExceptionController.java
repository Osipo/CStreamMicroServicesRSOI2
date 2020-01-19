package ru.osipov.deploy.errors.web;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.osipov.deploy.errors.HttpCanNotCreateException;
import ru.osipov.deploy.errors.HttpNotFoundException;
import ru.osipov.deploy.errors.HttpNotValueOfParameterException;
import ru.osipov.deploy.errors.ServiceAccessException;
import ru.osipov.deploy.errors.feign.*;
import ru.osipov.deploy.models.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Controller
public class ExceptionController extends AbstractErrorController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    public ExceptionController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> handleError(HttpServletRequest request) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, true);
        return errorAttributes;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exc) {
        List<FieldError> errors = exc.getBindingResult().getFieldErrors();
        String result = errors.stream().map(err -> String.format("Field %s has wrong value: [%s]", err.getField(), err.getDefaultMessage()))
                .collect(Collectors.joining("; "));
        logger.error("Bad Request: {}", result);
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), result);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpCanNotCreateException.class)
    @ResponseBody
    public ErrorResponse httpCanNotCreateExceptionHandler(HttpCanNotCreateException exc) {
        logger.error("Bad Request: {}", exc.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), exc.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException.class)
    @ResponseBody
    public ErrorResponse invalidFormatExceptionHandler(InvalidFormatException exc) {
        logger.error("Bad Request: {}", exc.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), exc.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(HttpNotFoundException.class)
    @ResponseBody
    public ErrorResponse httpNotFoundExceptionHandler(HttpNotFoundException exc) {
        logger.error("Not Found: {}", exc.getMessage());
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), exc.getMessage());
    }

    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    @ExceptionHandler(TimeoutException.class)
    @ResponseBody
    public ErrorResponse timeoutExceptionHandler(TimeoutException exc) {
        logger.error("Gateway Timeout: {}", exc.getMessage());
        return new ErrorResponse(HttpStatus.GATEWAY_TIMEOUT.value(), HttpStatus.GATEWAY_TIMEOUT.getReasonPhrase(), exc.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ErrorResponse missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException exc) {
        logger.error("Bad Request: {}", exc.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), exc.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpNotValueOfParameterException.class)
    @ResponseBody
    public ErrorResponse httpNotValueOfParameterExceptionHandler(HttpNotValueOfParameterException exc) {
        logger.error("Bad Request: {}", exc.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), exc.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ErrorResponse constraintViolationExceptionHandler(ConstraintViolationException exc) {
        logger.error("Bad Request: {}", exc.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), exc.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ErrorResponse methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException exc) {
        logger.error("Bad Request: {}", exc.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), exc.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ErrorResponse illegalArgumentExceptionHandler(IllegalArgumentException exc) {
        logger.error("Bad Request: {}", exc.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), exc.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomClientExceptionWrapper.class)
    @ResponseBody
    public ErrorResponse customClientExceptionHandler(CustomClientExceptionWrapper exc) {
        logger.error("Bad Request: {}", exc.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), exc.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ClientBadResponseExceptionWrapper.class)
    @ResponseBody
    public ErrorResponse clientBadResponseExceptionWrapper(ClientBadResponseExceptionWrapper exc) {
        logger.error("Bad Request: {}", exc.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), exc.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ClientNotFoundExceptionWrapper.class)
    @ResponseBody
    public ErrorResponse clientNotFoundExceptionWrapper(ClientNotFoundExceptionWrapper exc) {
        logger.error("Not Found: {}", exc.getMessage());
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), exc.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ErrorResponse jwtAuthenticationExceptionHandler(AuthenticationException exc) {
        logger.error("Unauthorized: {}", exc.getMessage());
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), exc.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ClientAuthenticationExceptionWrapper.class)
    @ResponseBody
    public ErrorResponse clientAuthenticationExceptionWrapper(ClientAuthenticationExceptionWrapper exc) {
        logger.error("Unauthorized: {}", exc.getMessage());
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), exc.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServiceAccessException.class)
    @ResponseBody
    public ErrorResponse serviceAccessExceptionHandler(ServiceAccessException exc) {
        logger.error("Interval Server Error: {}", exc.getMessage());
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exc.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(OtherServiceExceptionWrapper.class)
    @ResponseBody
    public ErrorResponse otherServiceExceptionWrapperHandler(OtherServiceExceptionWrapper exc) {
        logger.error("Interval Server Error: {}", exc.getMessage());
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exc.getMessage());
    }

   /* @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorResponse exceptionHandler(Exception exc) {
        logger.error("Interval Server Error: {}", exc.getMessage());
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exc.getMessage());
    }*/
}
