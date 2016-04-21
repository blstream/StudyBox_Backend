package com.bls.patronage.db.exception;

import javax.ws.rs.core.Response;

public class DataAccessException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final Integer defaultStatus = Response.Status.BAD_REQUEST.getStatusCode();
    private Integer status;

    public DataAccessException(String msg) {
        super(msg);
        this.status = defaultStatus;
    }

    public DataAccessException(String msg, Integer status) {
        super(msg);
        this.status = status;
    }

    public DataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public Integer getStatus() {
        return status;
    }
}
