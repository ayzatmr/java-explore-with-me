package ru.practicum.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.common.exception.AlreadyExistException;
import ru.practicum.common.exception.ObjectNotFoundException;
import ru.practicum.common.model.ErrorResponse;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({
            ConstraintViolationException.class,
            AlreadyExistException.class,
            DataIntegrityViolationException.class
    })
    @ResponseStatus(CONFLICT)
    public ErrorResponse handleConstraintViolationException(Exception e) {
        log.error(e.getLocalizedMessage());
        return ErrorResponse.builder()
                .errors(ExceptionUtils.getStackTrace(e))
                .message(ExceptionUtils.getMessage(e))
                .reason("Object already exists")
                .status(CONFLICT)
                .build();
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleNotFoundException(ObjectNotFoundException e) {
        log.error(e.getLocalizedMessage());
        return ErrorResponse.builder()
                .errors(ExceptionUtils.getStackTrace(e))
                .message(ExceptionUtils.getMessage(e))
                .reason("Object is not found")
                .status(NOT_FOUND)
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getLocalizedMessage());
        return ErrorResponse.builder()
                .errors(ExceptionUtils.getStackTrace(e))
                .message("Field: " + Objects.requireNonNull(e.getFieldError()).getField() +
                        " Error = " + Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage() +
                        " Value: " + e.getFieldError().getRejectedValue())
                .reason(ExceptionUtils.getMessage(e))
                .status(BAD_REQUEST)
                .build();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error(e.getLocalizedMessage());
        return ErrorResponse.builder()
                .errors(ExceptionUtils.getStackTrace(e))
                .message(e.getParameterName() + " is missing.")
                .reason("Missing request parameter " + e.getParameterName())
                .status(BAD_REQUEST)
                .build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConversionFailedException(MethodArgumentTypeMismatchException e) {
        return ErrorResponse.builder()
                .errors(ExceptionUtils.getStackTrace(e))
                .message(ExceptionUtils.getMessage(e))
                .reason("Wrong argument has been passed")
                .status(BAD_REQUEST)
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllException(Exception e) {
        log.error(e.getLocalizedMessage());
        return ErrorResponse.builder()
                .errors(ExceptionUtils.getStackTrace(e))
                .message(ExceptionUtils.getMessage(e))
                .reason("Unexpected error")
                .status(INTERNAL_SERVER_ERROR)
                .build();
    }
}