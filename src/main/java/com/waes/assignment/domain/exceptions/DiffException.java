package com.waes.assignment.domain.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Pedro on 26/05/2017.
 *
 * Exception class used for business logic errors
 */
@Getter
@AllArgsConstructor
public class DiffException extends RuntimeException {

    private String message;
}
