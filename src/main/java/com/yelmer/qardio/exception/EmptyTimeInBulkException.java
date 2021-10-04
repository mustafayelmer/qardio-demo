package com.yelmer.qardio.exception;

public class EmptyTimeInBulkException extends RuntimeException {
    public EmptyTimeInBulkException() {
        super("Time is empty in bulk operation!");
    }
}
