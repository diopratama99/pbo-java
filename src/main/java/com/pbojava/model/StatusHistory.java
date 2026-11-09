package com.pbojava.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Satu catatan perubahan status pengajuan: status barunya, kapan, dan admin yang melakukannya.
 * Catatan PENDING dibuat saat klien mengirim pengajuan, jadi admin-nya kosong.
 */
public class StatusHistory {

    private final RequestStatus status;
    private final LocalDateTime waktu;
    private final String adminNama;

    public StatusHistory(RequestStatus status, LocalDateTime waktu, String adminNama) {
        this.status = status;
        this.waktu = waktu;
        this.adminNama = adminNama;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public LocalDateTime getWaktu() {
        return waktu;
    }

    /** Nama admin yang mengubah status. null untuk catatan dari klien atau dari data lama. */
    public String getAdminNama() {
        return adminNama;
    }

    /** Contoh: "14 Okt 2026, 09.30" (id) atau "14 Oct 2026, 09:30" (en). */
    public String formatWaktu(String lang) {
        String pola = "en".equals(lang) ? "d MMM yyyy, HH:mm" : "d MMM yyyy, HH.mm";
        return waktu.format(DateTimeFormatter.ofPattern(pola, Locale.forLanguageTag(lang)));
    }
}
