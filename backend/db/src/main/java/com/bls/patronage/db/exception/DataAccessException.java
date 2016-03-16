package com.bls.patronage.db.exception;


public class DataAccessException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public DataAccessException(String msg) {
        super(msg);
    }

    public DataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
