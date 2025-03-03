package com.demo.smartpark.exception;

/**
 * @author jandrada
 */
public class NotFoundException extends ApplicationException {

    public NotFoundException() {
        super("No data found!");
    }

    public NotFoundException(String message) {
        super(message);
    }

}
