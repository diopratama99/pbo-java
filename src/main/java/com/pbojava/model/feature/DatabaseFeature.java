package com.pbojava.model.feature;

import com.pbojava.model.LocalizedText;

import java.math.BigDecimal;

/** Pilihan database. Harganya tetap. */
public class DatabaseFeature extends Feature {

    public DatabaseFeature(int id, LocalizedText nama, LocalizedText deskripsi, BigDecimal hargaDasar, boolean aktif) {
        super(id, nama, deskripsi, hargaDasar, aktif);
    }

    @Override
    public String getKategori() {
        return "DATABASE";
    }
}
