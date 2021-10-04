package com.yelmer.qardio.exception;

public class EmptyNameException extends RuntimeException {
    public EmptyNameException() {
        super("Name is empty!");
    }
}
