package com.interview.exception;

public class InvalidRequestException extends InterviewException {
    public InvalidRequestException(String message) {
        super(message, "INVALID_REQUEST");
    }
}
