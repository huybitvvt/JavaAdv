package com.bookstore.exception;

/**
 * Custom exception for Data Access Layer
 */
public class DataAccessException extends RuntimeException {

    private String sqlState;
    private int errorCode;

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataAccessException(String message, String sqlState, int errorCode) {
        super(message);
        this.sqlState = sqlState;
        this.errorCode = errorCode;
    }

    public String getSqlState() {
        return sqlState;
    }

    public void setSqlState(String sqlState) {
        this.sqlState = sqlState;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Check if it's a connection error
     */
    public boolean isConnectionError() {
        return sqlState != null && (sqlState.startsWith("08") || sqlState.equals("HY000"));
    }

    /**
     * Check if it's a constraint violation
     */
    public boolean isConstraintViolation() {
        return sqlState != null && sqlState.startsWith("23");
    }
}
