package com.pbojava.dao.jdbc;

import com.pbojava.model.LocalizedText;

/** Bantuan kecil yang dipakai bersama oleh semua DAO JDBC. */
final class JdbcUtil {

    private JdbcUtil() {
    }

    /** Teks dua bahasa dari dua kolom. Kalau versi Inggris kosong, pakai versi Indonesia. */
    static LocalizedText text(String id, String en) {
        String indonesia = id == null ? "" : id;
        return new LocalizedText(indonesia, en == null || en.isBlank() ? indonesia : en);
    }
}
