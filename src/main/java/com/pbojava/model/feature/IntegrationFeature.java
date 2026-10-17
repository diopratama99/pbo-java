package com.pbojava.model.feature;

import com.pbojava.model.LocalizedText;

import java.math.BigDecimal;

/** Integrasi layanan luar. Harga dasar ditambah biaya setup, tidak terpengaruh platform. */
public class IntegrationFeature extends Feature {

    private final BigDecimal biayaSetup;

    public IntegrationFeature(int id, LocalizedText nama, LocalizedText deskripsi, BigDecimal hargaDasar,
                              BigDecimal biayaSetup, boolean aktif) {
        super(id, nama, deskripsi, hargaDasar, aktif);
        this.biayaSetup = biayaSetup;
    }

    @Override
    public String getKategori() {
        return "INTEGRATION";
    }

    public BigDecimal getBiayaSetup() {
        return biayaSetup;
    }
}
