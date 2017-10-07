package org.keithkim.stixdb.core;

public class StixException extends RuntimeException {

    public StixException(String message) {
        super(message);
    }

    public StixException(Throwable cause) {
        super(cause);
    }

    protected StixException(String message, Throwable cause) {
        super(message, cause);
    }

    protected StixException(String message, Throwable cause,
                            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
