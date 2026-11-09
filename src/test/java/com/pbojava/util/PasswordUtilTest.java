package com.pbojava.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordUtilTest {

    @Test
    void passwordYangBenarCocok() {
        String salt = PasswordUtil.buatSalt();
        String hash = PasswordUtil.hash("rahasia-banget", salt);

        assertTrue(PasswordUtil.cocok("rahasia-banget", hash, salt));
        assertFalse(PasswordUtil.cocok("rahasia-bangeT", hash, salt));
        assertFalse(PasswordUtil.cocok(null, hash, salt));
    }

    @Test
    void saltBerbedaMenghasilkanHashBerbeda() {
        String hash1 = PasswordUtil.hash("sama", PasswordUtil.buatSalt());
        String hash2 = PasswordUtil.hash("sama", PasswordUtil.buatSalt());

        assertNotEquals(hash1, hash2);
    }
}
