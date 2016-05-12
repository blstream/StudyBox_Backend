package com.bls.patronage.exception;

public class PasswordResetException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PasswordResetException(String msg) {
        super(msg);
    }

    public PasswordResetException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
