package com.randomrainbow.springboot.demosecurity.util;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}