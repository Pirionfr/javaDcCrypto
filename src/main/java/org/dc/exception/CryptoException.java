package org.dc.exception;

/**
 * Created by dcapitai on 5/17/18.
 */
public class CryptoException extends RuntimeException {
    public CryptoException(String message) {
        super(message);
    }

    public CryptoException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
