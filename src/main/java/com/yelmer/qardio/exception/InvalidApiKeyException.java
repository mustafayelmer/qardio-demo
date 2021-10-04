package com.yelmer.qardio.exception;

public class InvalidApiKeyException extends RuntimeException {
    public InvalidApiKeyException(String value) {
        super("Api-key is empty or invalid with " + value + "!");
    }
}
