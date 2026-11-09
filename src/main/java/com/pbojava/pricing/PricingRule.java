package com.pbojava.pricing;

import com.pbojava.model.feature.Feature;

import java.math.BigDecimal;
import java.util.List;

/**
 * Aturan diskon. Aturan baru cukup dibuat sebagai class baru yang meng-implement interface ini,
 * tanpa mengubah QuotationCalculator.
 */
public interface PricingRule {

    /** Besar diskon (bukan persen) untuk subtotal dan fitur yang dipilih. 0 kalau tidak berlaku. */
    BigDecimal hitungDiskon(BigDecimal subtotal, List<Feature> fiturDipilih);
}
