package com.github.kotvertolet.podegen.core.exceptions;

public class PodegenException extends Error {

    public PodegenException(String message) {
        super(message);
    }

    public PodegenException(String message, Throwable cause) {
        super(message, cause);
    }

    public PodegenException(Throwable cause) {
        super(cause);
    }
}
