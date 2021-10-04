package com.yelmer.qardio.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String key, String value) {
        super("Device was not found with " + key + ": " + value);
    }
}
