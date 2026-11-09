package com.pbojava.util;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TicketGeneratorTest {

    @Test
    void formatnyaDaDitambahEnamKarakter() {
        for (int i = 0; i < 100; i++) {
            String kode = TicketGenerator.buat();
            assertTrue(kode.matches("DA-[A-HJKMNP-Z2-9]{6}"), kode);
        }
    }

    @Test
    void kodenyaAcak() {
        Set<String> kode = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            kode.add(TicketGenerator.buat());
        }
        // Peluang kode kembar dari 31^6 kombinasi sangat kecil
        assertTrue(kode.size() > 990);
    }
}
