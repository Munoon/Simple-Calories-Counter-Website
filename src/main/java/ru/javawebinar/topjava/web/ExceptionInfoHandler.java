package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.ErrorType;
import ru.javawebinar.topjava.util.exception.IllegalRequestDataException;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.exception.ErrorType.*;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    @Autowired
    private MessageSource messageSource;

    //  http://stackoverflow.com/a/22358422/548473
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, DATA_NOT_FOUND);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String errorMessage = e.getRootCause().getMessage();
        Locale locale = LocaleContextHolder.getLocale();

        if (errorMessage.contains("users_unique_email_idx")) {
            log.error("Database error users_unique_email_idx");
            String message = messageSource.getMessage("error.notUniqueEmail", null, locale);
            return new ErrorInfo(req.getRequestURL(), DATA_ERROR, message);
        } else if (errorMessage.contains("meals_unique_user_datetime_idx")) {
            log.error("Database error meals_unique_user_datetime_idx");
            String message = messageSource.getMessage("error.notUniqueMealDate", null, locale);
            return new ErrorInfo(req.getRequestURL(), DATA_ERROR, message);
        }

        return logAndGetErrorInfo(req, e, true, DATA_ERROR);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler({IllegalRequestDataException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ErrorInfo illegalRequestDataError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorInfo beanValidationExceptionHandler(HttpServletRequest req, BindException e) {
        List<String> errors = getErrorsFieldList(e.getFieldErrors());
        return logAndGetErrorInfo(req, errors, true, VALIDATION_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorInfo argumentNotValidHandler(HttpServletRequest req, MethodArgumentNotValidException e) {
        List<String> errors = getErrorsFieldList(e.getBindingResult().getFieldErrors());
        return logAndGetErrorInfo(req, errors, true, VALIDATION_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, APP_ERROR);
    }

    // https://stackoverflow.com/questions/538870/should-private-helper-methods-be-static-if-they-can-be-static
    private static ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException, ErrorType errorType) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logException) {
            log.error(errorType + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        }
        return logAndGetErrorInfo(req, rootCause.toString(), false, errorType);
    }

    private static ErrorInfo logAndGetErrorInfo(HttpServletRequest req, String message, boolean logException, ErrorType errorType) {
        if (logException)
            log.warn("{} at request: {}", errorType, req.getRequestURL());
        return new ErrorInfo(req.getRequestURL(), errorType, message);
    }

    private static ErrorInfo logAndGetErrorInfo(HttpServletRequest req, List<String> details, boolean logException, ErrorType errorType) {
        if (logException)
            log.warn("{} at request: {}", errorType, req.getRequestURL());
        return new ErrorInfo(req.getRequestURL(), errorType, details);
    }

    private static List<String> getErrorsFieldList(List<FieldError> errors) {
        return errors.stream()
                .map(error -> String.format("%s - %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
    }
}