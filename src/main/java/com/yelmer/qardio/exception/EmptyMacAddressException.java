package com.yelmer.qardio.exception;

public class EmptyMacAddressException extends RuntimeException {
    public EmptyMacAddressException() {
        super("Mac address is empty!");
    }
}
