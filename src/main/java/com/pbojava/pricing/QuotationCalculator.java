package com.pbojava.pricing;

import com.pbojava.model.RequestFeature;
import com.pbojava.model.feature.DatabaseFeature;
import com.pbojava.model.feature.Feature;
import com.pbojava.model.feature.PlatformFeature;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Menghitung estimasi harga di server.
 * Angka dari browser tidak dipakai, karena bisa diubah lewat inspect element.
 */
public class QuotationCalculator {

    private final List<PricingRule> rules;

    public QuotationCalculator(List<PricingRule> rules) {
        this.rules = List.copyOf(rules);
    }

    /** Kalkulator dengan aturan yang berlaku sekarang: diskon bundling. */
    public static QuotationCalculator standar() {
        return new QuotationCalculator(List.of(new BundleDiscountRule()));
    }

    /**
     * @param fitur fitur utama dan integrasi yang dipilih (boleh kosong)
     */
    public Quotation hitung(PlatformFeature platform, DatabaseFeature database, List<Feature> fitur) {
        List<Feature> semua = new ArrayList<>();
        semua.add(platform);
        semua.add(database);
        semua.addAll(fitur);

        List<RequestFeature> rincian = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        for (Feature feature : semua) {
            // Polymorphism: tiap subclass menghitung harganya sendiri
            BigDecimal harga = feature.hitungHarga(platform);
            rincian.add(new RequestFeature(feature, harga));
            subtotal = subtotal.add(harga);
        }

        BigDecimal diskon = BigDecimal.ZERO;
        for (PricingRule rule : rules) {
            diskon = diskon.add(rule.hitungDiskon(subtotal, semua));
        }
        // Total tidak boleh minus kalau nanti ada beberapa aturan diskon sekaligus
        diskon = diskon.min(subtotal);

        return new Quotation(rincian, subtotal, diskon);
    }
}
