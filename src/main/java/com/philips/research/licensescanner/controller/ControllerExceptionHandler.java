/*
 * Copyright (c) 2020-2021, Koninklijke Philips N.V., https://www.philips.com
 * SPDX-License-Identifier: MIT
 */

package com.philips.research.licensescanner.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Converts exceptions on REST requests into status responses.
 */
@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * Handles requested resources that are not available on the server.
     *
     * @return NOT_FOUND status with a list of resources that were not found.
     */
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public Map<String, String> handleNotFoundException(ResourceNotFoundException exception) {
        LOG.info("Resource '{}' was not found", exception.getResource());
        return Map.of("resource", exception.getResource());
    }

    /**
     * Handles request parameter validation failures.
     *
     * @return BAD_REQUEST with a list of the detected validation failures.
     */
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception) {
        //noinspection ConstantConditions
        return exception.getBindingResult().getAllErrors().stream()
                .collect(Collectors.toMap(
                        (error) -> ((FieldError) error).getField(),
                        DefaultMessageSourceResolvable::getDefaultMessage));
    }

    /**
     * Handles request type validation failures.
     *
     * @return BAD_REQUEST with a list of the detected validation failures.
     */
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public Map<String, String> handleArgumentTypExceptions(MethodArgumentTypeMismatchException exception) {
        //noinspection ConstantConditions
        return Map.of(exception.getName(), exception.getValue().toString());
    }
}
