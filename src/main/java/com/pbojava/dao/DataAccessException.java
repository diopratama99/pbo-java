package com.pbojava.dao;

/**
 * Error saat membaca atau menyimpan data, misalnya database mati.
 * Dibuat unchecked supaya interface DAO tidak bergantung pada SQLException milik JDBC.
 */
public class DataAccessException extends RuntimeException {

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
