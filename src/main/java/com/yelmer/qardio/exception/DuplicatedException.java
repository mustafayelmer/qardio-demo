package com.yelmer.qardio.exception;

public class DuplicatedException extends RuntimeException {
    public DuplicatedException(String key, String value) {
        super("Device was duplicated for " + key + ": " + value);
    }
}
