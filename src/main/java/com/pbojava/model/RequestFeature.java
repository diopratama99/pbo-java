package com.pbojava.model;

import com.pbojava.model.feature.Feature;

import java.math.BigDecimal;

/** Fitur yang dipilih di sebuah pengajuan, beserta harganya saat pengajuan dibuat. */
public class RequestFeature {

    private final Feature feature;
    private final BigDecimal hargaSaatItu;

    public RequestFeature(Feature feature, BigDecimal hargaSaatItu) {
        this.feature = feature;
        this.hargaSaatItu = hargaSaatItu;
    }

    public Feature getFeature() {
        return feature;
    }

    public BigDecimal getHargaSaatItu() {
        return hargaSaatItu;
    }
}
