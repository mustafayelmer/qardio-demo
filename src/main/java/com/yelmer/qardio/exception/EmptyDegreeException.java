package com.yelmer.qardio.exception;

public class EmptyDegreeException extends RuntimeException {
    public EmptyDegreeException() {
        super("Degree is empty!");
    }
}
