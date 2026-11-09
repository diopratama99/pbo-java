package com.pbojava.util;

import java.security.SecureRandom;

/**
 * Membuat kode tiket acak, contoh: DA-7K2P9Q.
 * Huruf dan angka yang mirip (0/O, 1/I/L) tidak dipakai supaya kodenya tidak salah ketik.
 */
public final class TicketGenerator {

    public static final String PREFIX = "DA-";
    private static final String KARAKTER = "ABCDEFGHJKMNPQRSTUVWXYZ23456789";
    private static final int PANJANG = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    private TicketGenerator() {
    }

    public static String buat() {
        StringBuilder kode = new StringBuilder(PREFIX);
        for (int i = 0; i < PANJANG; i++) {
            kode.append(KARAKTER.charAt(RANDOM.nextInt(KARAKTER.length())));
        }
        return kode.toString();
    }
}
