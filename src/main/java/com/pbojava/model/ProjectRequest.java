package com.pbojava.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Pengajuan proyek dari klien.
 * Prototype: belum ada method approve(), reject(), dll. Aturan perubahan status dibuat di tahap berikutnya.
 */
public class ProjectRequest {

    private final int id;
    private final String kodeTiket;
    private final Client client;
    private final List<RequestFeature> fitur;
    private final String customNotes;
    private final BigDecimal subtotal;
    private final BigDecimal diskon;
    private final BigDecimal totalEstimasi;
    private final RequestStatus status;
    private final String wagLink;
    private final String alasanDitolak;
    private final LocalDateTime createdAt;

    public ProjectRequest(int id, String kodeTiket, Client client, List<RequestFeature> fitur, String customNotes,
                          BigDecimal subtotal, BigDecimal diskon, BigDecimal totalEstimasi, RequestStatus status,
                          String wagLink, String alasanDitolak, LocalDateTime createdAt) {
        this.id = id;
        this.kodeTiket = kodeTiket;
        this.client = client;
        this.fitur = fitur;
        this.customNotes = customNotes;
        this.subtotal = subtotal;
        this.diskon = diskon;
        this.totalEstimasi = totalEstimasi;
        this.status = status;
        this.wagLink = wagLink;
        this.alasanDitolak = alasanDitolak;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getKodeTiket() {
        return kodeTiket;
    }

    public Client getClient() {
        return client;
    }

    public List<RequestFeature> getFitur() {
        return fitur;
    }

    public String getCustomNotes() {
        return customNotes;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getDiskon() {
        return diskon;
    }

    public BigDecimal getTotalEstimasi() {
        return totalEstimasi;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public String getWagLink() {
        return wagLink;
    }

    public String getAlasanDitolak() {
        return alasanDitolak;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** Tanggal dibuat sesuai bahasa. Contoh: "12 Okt 2026" (id) atau "12 Oct 2026" (en). */
    public String formatTanggal(String lang) {
        return createdAt.format(DateTimeFormatter.ofPattern("d MMM yyyy", Locale.forLanguageTag(lang)));
    }
}
