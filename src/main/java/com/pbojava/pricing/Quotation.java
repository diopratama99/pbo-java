package com.pbojava.pricing;

import com.pbojava.model.RequestFeature;

import java.math.BigDecimal;
import java.util.List;

/** Hasil hitungan QuotationCalculator: harga tiap fitur, subtotal, diskon, dan total. */
public class Quotation {

    private final List<RequestFeature> rincian;
    private final BigDecimal subtotal;
    private final BigDecimal diskon;
    private final BigDecimal total;

    public Quotation(List<RequestFeature> rincian, BigDecimal subtotal, BigDecimal diskon) {
        this.rincian = List.copyOf(rincian);
        this.subtotal = subtotal;
        this.diskon = diskon;
        this.total = subtotal.subtract(diskon);
    }

    /** Setiap fitur yang dipilih beserta harganya. Disimpan ke tabel request_features. */
    public List<RequestFeature> getRincian() {
        return rincian;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getDiskon() {
        return diskon;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
