package com.pbojava.pricing;

import com.pbojava.model.feature.Feature;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Diskon bundling: memilih minimal 5 fitur (utama + integrasi) dapat diskon 10% dari subtotal.
 * Angkanya harus sama dengan BUNDLE_MIN dan BUNDLE_RATE di assets/js/estimate.js.
 */
public class BundleDiscountRule implements PricingRule {

    public static final int MINIMAL_FITUR = 5;
    public static final BigDecimal PERSEN = new BigDecimal("0.10");

    @Override
    public BigDecimal hitungDiskon(BigDecimal subtotal, List<Feature> fiturDipilih) {
        long jumlah = fiturDipilih.stream()
                .filter(feature -> feature.getKategori().equals("CORE") || feature.getKategori().equals("INTEGRATION"))
                .count();
        if (jumlah < MINIMAL_FITUR) {
            return BigDecimal.ZERO;
        }
        return subtotal.multiply(PERSEN).setScale(0, RoundingMode.HALF_UP);
    }
}
