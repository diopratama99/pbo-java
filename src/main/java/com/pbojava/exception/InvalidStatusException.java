package com.pbojava.exception;

/** Dilempar saat perubahan status pengajuan tidak sesuai alur di plan.md bagian 5. */
public class InvalidStatusException extends Exception {

    public InvalidStatusException(String message) {
        super(message);
    }
}
