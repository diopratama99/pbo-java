package com.pbojava.model;

import com.pbojava.exception.InvalidStatusException;
import com.pbojava.exception.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Pengajuan proyek dari klien.
 *
 * Status tidak punya setter. Status hanya bisa diubah lewat mulaiReview(), approve(), dan reject(),
 * supaya alur di plan.md bagian 5 tetap berlaku dari mana pun method-nya dipanggil:
 * PENDING → REVIEW → APPROVED, dan PENDING/REVIEW → REJECTED. APPROVED dan REJECTED adalah status akhir.
 */
public class ProjectRequest {

    public static final String WAG_PREFIX = "https://chat.whatsapp.com/";
    public static final int MAKS_PANJANG_TEKS = 255;

    private final int id;
    private final String kodeTiket;
    private final Client client;
    private final List<RequestFeature> fitur;
    private final String customNotes;
    private final BigDecimal subtotal;
    private final BigDecimal diskon;
    private final BigDecimal totalEstimasi;
    private RequestStatus status;
    private String wagLink;
    private String alasanDitolak;
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

    /** PENDING → REVIEW. */
    public void mulaiReview() throws InvalidStatusException {
        if (status != RequestStatus.PENDING) {
            throw new InvalidStatusException("Hanya pengajuan PENDING yang bisa mulai direview, status sekarang " + status);
        }
        status = RequestStatus.REVIEW;
    }

    /** REVIEW → APPROVED. Link grup WhatsApp wajib diisi. */
    public void approve(String link) throws InvalidStatusException, ValidationException {
        if (status != RequestStatus.REVIEW) {
            throw new InvalidStatusException("Hanya pengajuan REVIEW yang bisa disetujui, status sekarang " + status);
        }
        String bersih = link == null ? "" : link.trim();
        if (!bersih.startsWith(WAG_PREFIX) || bersih.length() == WAG_PREFIX.length()
                || bersih.length() > MAKS_PANJANG_TEKS || bersih.contains(" ")) {
            throw new ValidationException("validation.wag");
        }
        wagLink = bersih;
        status = RequestStatus.APPROVED;
    }

    /** PENDING/REVIEW → REJECTED. Alasan wajib diisi. Boleh langsung dari PENDING, misalnya untuk spam. */
    public void reject(String alasan) throws InvalidStatusException, ValidationException {
        if (status != RequestStatus.PENDING && status != RequestStatus.REVIEW) {
            throw new InvalidStatusException("Pengajuan " + status + " sudah final dan tidak bisa ditolak");
        }
        String bersih = alasan == null ? "" : alasan.trim();
        if (bersih.isEmpty() || bersih.length() > MAKS_PANJANG_TEKS) {
            throw new ValidationException("admin.error.reason");
        }
        alasanDitolak = bersih;
        status = RequestStatus.REJECTED;
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

    /** Fitur yang dipilih. Kosong kalau pengajuan diambil lewat daftar (findAll / findByStatus). */
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
