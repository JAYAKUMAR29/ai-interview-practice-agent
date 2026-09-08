package com.interview.exception;

public class ResourceNotFoundException extends InterviewException {
    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }
}
