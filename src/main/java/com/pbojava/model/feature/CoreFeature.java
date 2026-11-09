package com.pbojava.model.feature;

import com.pbojava.model.LocalizedText;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Fitur utama. Harganya dikali pengali platform yang dipilih. */
public class CoreFeature extends Feature {

    public CoreFeature(int id, LocalizedText nama, LocalizedText deskripsi, BigDecimal hargaDasar, boolean aktif) {
        super(id, nama, deskripsi, hargaDasar, aktif);
    }

    @Override
    public String getKategori() {
        return "CORE";
    }

    /** Dibulatkan ke rupiah penuh, sama seperti yang ditampilkan di kalkulator. */
    @Override
    public BigDecimal hitungHarga(PlatformFeature platform) {
        return getHargaDasar().multiply(platform.getPengali()).setScale(0, RoundingMode.HALF_UP);
    }
}
