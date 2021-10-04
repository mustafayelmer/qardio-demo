package com.yelmer.qardio.error;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

/**
 * Error response for rest api
 * */
@Getter
@Setter
public class ErrorResponse {
    private String message;
    private String name;
    private HashSet<String> others;

    public ErrorResponse() {
        this.others = new HashSet<>();
    }

    public ErrorResponse(Exception e) {
        super();
        this.name = e.getClass().getName();
        this.message = e.getLocalizedMessage();
        this.others.add(e.getMessage());
    }

    public ErrorResponse(Exception e, String message) {
        super();
        this.name = e.getClass().getName();
        this.message = message;
        this.others.add(e.getLocalizedMessage());
        this.others.add(e.getMessage());
    }

    public ErrorResponse(Exception e, String message, String name) {
        super();
        this.name = name;
        this.message = message;
        this.others.add(e.getLocalizedMessage());
        this.others.add(e.getMessage());
    }

    public void add(String part) {
        this.others.add(part);
    }
}
