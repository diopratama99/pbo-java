package com.pbojava.model.feature;

import com.pbojava.model.LocalizedText;

import java.math.BigDecimal;

/**
 * Fitur yang bisa dipilih klien di kalkulator.
 * Tidak bisa dibuat langsung, harus lewat salah satu dari 4 subclass-nya.
 */
public abstract class Feature {

    private final int id;
    private final LocalizedText nama;
    private final LocalizedText deskripsi;
    private final BigDecimal hargaDasar;
    private final boolean aktif;

    protected Feature(int id, LocalizedText nama, LocalizedText deskripsi, BigDecimal hargaDasar, boolean aktif) {
        this.id = id;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.hargaDasar = hargaDasar;
        this.aktif = aktif;
    }

    /** PLATFORM, DATABASE, CORE, atau INTEGRATION. Sama dengan kolom kategori di tabel master_features. */
    public abstract String getKategori();

    public int getId() {
        return id;
    }

    public LocalizedText getNama() {
        return nama;
    }

    public LocalizedText getDeskripsi() {
        return deskripsi;
    }

    public BigDecimal getHargaDasar() {
        return hargaDasar;
    }

    public boolean isAktif() {
        return aktif;
    }
}
