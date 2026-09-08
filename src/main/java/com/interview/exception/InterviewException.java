package com.interview.exception;

public class InterviewException extends RuntimeException {
    private final String errorCode;

    public InterviewException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
