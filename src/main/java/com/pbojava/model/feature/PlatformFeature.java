package com.pbojava.model.feature;

import com.pbojava.model.LocalizedText;

import java.math.BigDecimal;

/** Platform aplikasi. Punya pengali yang memengaruhi harga fitur utama. */
public class PlatformFeature extends Feature {

    private final BigDecimal pengali;

    public PlatformFeature(int id, LocalizedText nama, LocalizedText deskripsi, BigDecimal hargaDasar,
                           BigDecimal pengali, boolean aktif) {
        super(id, nama, deskripsi, hargaDasar, aktif);
        this.pengali = pengali;
    }

    @Override
    public String getKategori() {
        return "PLATFORM";
    }

    public BigDecimal getPengali() {
        return pengali;
    }
}
