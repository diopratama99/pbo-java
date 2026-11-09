package com.pbojava.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Hash password admin dengan PBKDF2 (HMAC-SHA256) + salt acak.
 * Hasilnya disimpan di kolom admins.password_hash dan admins.salt dalam bentuk Base64.
 *
 * PBKDF2 sengaja dibuat lambat (banyak iterasi), supaya password tidak mudah ditebak
 * walaupun isi database bocor.
 */
public final class PasswordUtil {

    private static final String ALGORITMA = "PBKDF2WithHmacSHA256";
    private static final int ITERASI = 120_000;
    private static final int PANJANG_HASH_BIT = 256;
    private static final int PANJANG_SALT_BYTE = 16;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() {
    }

    public static String buatSalt() {
        byte[] salt = new byte[PANJANG_SALT_BYTE];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hash(String password, String salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), Base64.getDecoder().decode(salt),
                    ITERASI, PANJANG_HASH_BIT);
            byte[] hasil = SecretKeyFactory.getInstance(ALGORITMA).generateSecret(spec).getEncoded();
            spec.clearPassword();
            return Base64.getEncoder().encodeToString(hasil);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Algoritma " + ALGORITMA + " tidak tersedia", e);
        }
    }

    /** Dibandingkan dengan MessageDigest.isEqual supaya lama prosesnya tidak membocorkan isi hash. */
    public static boolean cocok(String password, String hashTersimpan, String salt) {
        if (password == null || hashTersimpan == null || salt == null) {
            return false;
        }
        byte[] dihitung = Base64.getDecoder().decode(hash(password, salt));
        byte[] tersimpan = Base64.getDecoder().decode(hashTersimpan);
        return MessageDigest.isEqual(dihitung, tersimpan);
    }
}
