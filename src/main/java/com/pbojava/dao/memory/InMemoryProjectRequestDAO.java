package com.pbojava.dao.memory;

import com.pbojava.dao.FeatureDAO;
import com.pbojava.dao.ProjectRequestDAO;
import com.pbojava.model.Client;
import com.pbojava.model.ProjectRequest;
import com.pbojava.model.RequestFeature;
import com.pbojava.model.RequestStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Data pengajuan contoh, satu untuk tiap status, supaya halaman cek status dan admin bisa langsung dicoba.
 *
 * Tiket untuk demo cek status (email: klien@contoh.com):
 * DA-PEND01 (PENDING), DA-REVW01 (REVIEW), DA-APPR01 (APPROVED), DA-REJT01 (REJECTED).
 *
 * Harga di sini dihitung manual mengikuti plan.md bagian 6.
 */
public class InMemoryProjectRequestDAO implements ProjectRequestDAO {

    private final FeatureDAO featureDAO;
    private final List<ProjectRequest> requests = new ArrayList<>();

    public InMemoryProjectRequestDAO(FeatureDAO featureDAO) {
        this.featureDAO = featureDAO;

        Client rina = new Client(1, "Rina Wijaya", "Toko Rina", "klien@contoh.com", "081234567890");
        Client budi = new Client(2, "Budi Santoso", "CV Maju Jaya", "budi@contoh.com", "081298765432");
        Client sari = new Client(3, "Sari Lestari", null, "sari@contoh.com", "085711223344");

        // Website, MySQL, Login (x1.0), Dashboard Admin (x1.0)
        requests.add(new ProjectRequest(1, "DA-PEND01", rina,
                fitur(1, "3000000", 4, "1000000", 6, "1500000", 7, "2500000"),
                "Butuh fitur cetak struk untuk kasir.",
                rp("8000000"), rp("0"), rp("8000000"), RequestStatus.PENDING,
                null, null, LocalDateTime.of(2026, 10, 14, 9, 30)));

        // Contoh hitungan di plan.md: Mobile, MySQL, Login (x1.3), Chat (x1.3), Payment Gateway (+ setup)
        requests.add(new ProjectRequest(2, "DA-REVW01", rina,
                fitur(2, "5000000", 4, "1000000", 6, "1950000", 8, "3900000", 12, "2500000"),
                null,
                rp("14350000"), rp("0"), rp("14350000"), RequestStatus.REVIEW,
                null, null, LocalDateTime.of(2026, 10, 12, 14, 5)));

        // Website + Mobile (x1.6) dengan 5 fitur, jadi dapat diskon bundling 10%
        requests.add(new ProjectRequest(3, "DA-APPR01", rina,
                fitur(3, "7000000", 5, "1000000", 6, "2400000", 7, "4000000", 9, "1600000",
                        10, "2400000", 13, "1250000"),
                "Aplikasi untuk 3 cabang toko.",
                rp("19650000"), rp("1965000"), rp("17685000"), RequestStatus.APPROVED,
                "https://chat.whatsapp.com/ContohLinkGrup", null, LocalDateTime.of(2026, 10, 8, 10, 0)));

        requests.add(new ProjectRequest(4, "DA-REJT01", rina,
                fitur(1, "3000000", 4, "1000000", 8, "3000000"),
                "Mau seperti aplikasi besar.",
                rp("7000000"), rp("0"), rp("7000000"), RequestStatus.REJECTED,
                null, "Kebutuhannya belum jelas. Silakan ajukan ulang dengan daftar fitur yang lebih detail.",
                LocalDateTime.of(2026, 10, 5, 16, 45)));

        requests.add(new ProjectRequest(5, "DA-8F3K2M", budi,
                fitur(2, "5000000", 4, "1000000", 6, "1950000", 9, "1300000"),
                null,
                rp("9250000"), rp("0"), rp("9250000"), RequestStatus.PENDING,
                null, null, LocalDateTime.of(2026, 10, 15, 8, 10)));

        requests.add(new ProjectRequest(6, "DA-Q7N4TX", sari,
                fitur(1, "3000000", 5, "1000000", 11, "1000000", 15, "1000000"),
                "Website katalog produk handmade.",
                rp("6000000"), rp("0"), rp("6000000"), RequestStatus.REVIEW,
                null, null, LocalDateTime.of(2026, 10, 13, 19, 20)));

        requests.sort(Comparator.comparing(ProjectRequest::getCreatedAt).reversed());
    }

    @Override
    public List<ProjectRequest> findAll() {
        return requests;
    }

    @Override
    public List<ProjectRequest> findByStatus(RequestStatus status) {
        return requests.stream().filter(request -> request.getStatus() == status).toList();
    }

    @Override
    public Optional<ProjectRequest> findById(int id) {
        return requests.stream().filter(request -> request.getId() == id).findFirst();
    }

    @Override
    public Optional<ProjectRequest> findByTicketAndEmail(String kodeTiket, String email) {
        return requests.stream()
                .filter(request -> request.getKodeTiket().equalsIgnoreCase(kodeTiket.trim()))
                .filter(request -> request.getClient().getEmail().equalsIgnoreCase(email.trim()))
                .findFirst();
    }

    /** Pasangan (id fitur, harga saat itu). Contoh: fitur(1, "3000000", 4, "1000000"). */
    private List<RequestFeature> fitur(Object... pasangan) {
        List<RequestFeature> hasil = new ArrayList<>();
        for (int i = 0; i < pasangan.length; i += 2) {
            int featureId = (Integer) pasangan[i];
            BigDecimal harga = rp((String) pasangan[i + 1]);
            featureDAO.findById(featureId).ifPresent(feature -> hasil.add(new RequestFeature(feature, harga)));
        }
        return hasil;
    }

    private static BigDecimal rp(String nilai) {
        return new BigDecimal(nilai);
    }
}
