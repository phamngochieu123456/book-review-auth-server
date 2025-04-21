package com.hieupn.auth.exception;

public class DuplicateResourceException extends ApiException {
    public DuplicateResourceException(String resource, String field, Object value) {
        super(String.format("%s already exists with %s: %s", resource, field, value));
    }
}