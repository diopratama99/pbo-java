package com.pbojava.controller;

import java.math.BigDecimal;

/** Bantuan membaca isian form di halaman admin. */
final class AdminForms {

    private AdminForms() {
    }

    static String trim(String value) {
        return value == null ? "" : value.trim();
    }

    /** 0 kalau kosong atau bukan angka. Id 0 berarti data baru. */
    static int parseInt(String value) {
        try {
            return Integer.parseInt(trim(value));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Nominal rupiah dari input data-money, misalnya "3.000.000" atau "3,000,000".
     * Pemisah ribuan dibuang. null kalau kosong atau terlalu panjang.
     */
    static BigDecimal parseRupiah(String value) {
        String angka = trim(value).replaceAll("[^0-9]", "");
        if (angka.isEmpty() || angka.length() > 13) {
            return null;
        }
        return new BigDecimal(angka);
    }

    /** Angka desimal biasa, misalnya pengali "1.30". null kalau bukan angka. */
    static BigDecimal parseDecimal(String value) {
        try {
            return new BigDecimal(trim(value).replace(',', '.'));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
