package com.waes.assignment.gateway;

import com.waes.assignment.domain.exceptions.DiffException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class responsible for handling know exceptions and generating friendly return messages
 */
@ControllerAdvice
public class CustomErrorHandler {

    /**
     * Handle handleMethodArgumentNotValidException validation exception
     *
     * @param exception  Exception thrown when request data does not have the required attributes
     * @return The following message: Your request json is missing the following attributes: [<list of missing attributes>]
     */
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Error handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        StringBuilder sb = new StringBuilder();
        sb.append("Your request json is missing the following attributes: ");
        sb.append(fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", ")));
        return new Error(sb.toString());
    }

    /**
     * Handle DiffException exception
     *
     * @param exception  Exception thrown for diff business logic problems
     * @return The message of the exception thrown
     */
    @ResponseStatus(value= HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(DiffException.class)
    @ResponseBody
    public Error conflict(DiffException exception) {
        return new Error(exception.getMessage());
    }

    class Error {
        private String message;

        public Error(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

}
