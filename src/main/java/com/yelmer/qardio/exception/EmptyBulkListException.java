package com.yelmer.qardio.exception;

public class EmptyBulkListException extends RuntimeException {
    public EmptyBulkListException() {
        super("Bulk array is empty or null!");
    }
}
