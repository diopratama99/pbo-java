package com.pbojava.exception;

/**
 * Isian form tidak valid.
 * {@code messageKey} adalah key di file terjemahan, supaya pesannya bisa ditampilkan sesuai bahasa.
 */
public class ValidationException extends Exception {

    private final String messageKey;

    public ValidationException(String messageKey) {
        super(messageKey);
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
