package com.yelmer.qardio.exception;

public class OutOfRangeDegreeException extends RuntimeException {
    public OutOfRangeDegreeException(Double degree) {
        super("Degree is out of range with value : " + degree);
    }
}
