package com.pbojava.service;

import com.pbojava.dao.FeatureDAO;
import com.pbojava.dao.ProjectRequestDAO;
import com.pbojava.exception.InvalidStatusException;
import com.pbojava.exception.ValidationException;
import com.pbojava.model.Admin;
import com.pbojava.model.Client;
import com.pbojava.model.ProjectRequest;
import com.pbojava.model.RequestStatus;
import com.pbojava.model.feature.DatabaseFeature;
import com.pbojava.model.feature.Feature;
import com.pbojava.model.feature.PlatformFeature;
import com.pbojava.pricing.Quotation;
import com.pbojava.pricing.QuotationCalculator;
import com.pbojava.util.TicketGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Aturan bisnis pengajuan: kirim pengajuan baru dan ubah statusnya.
 * Servlet cukup meneruskan isian form ke sini.
 */
public class RequestService {

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    /** Sama dengan pattern di input noWa (estimate.jsp): angka, +, dan spasi. */
    private static final Pattern NO_WA = Pattern.compile("^[0-9+ ]{9,20}$");
    private static final int MAKS_CATATAN = 2000;
    private static final int MAKS_PERCOBAAN_TIKET = 10;

    private final FeatureDAO featureDAO;
    private final ProjectRequestDAO requestDAO;
    private final QuotationCalculator calculator;

    public RequestService(FeatureDAO featureDAO, ProjectRequestDAO requestDAO, QuotationCalculator calculator) {
        this.featureDAO = featureDAO;
        this.requestDAO = requestDAO;
        this.calculator = calculator;
    }

    /**
     * Validasi isian, hitung ulang harga di server, lalu simpan.
     * Mengembalikan kode tiket untuk ditampilkan ke klien.
     */
    public String kirim(PengajuanForm form) throws ValidationException {
        PlatformFeature platform = fiturAktif(form.platformId(), PlatformFeature.class)
                .orElseThrow(() -> new ValidationException("validation.platform"));
        DatabaseFeature database = fiturAktif(form.databaseId(), DatabaseFeature.class)
                .orElseThrow(() -> new ValidationException("validation.database"));

        List<Feature> fitur = new ArrayList<>();
        Set<Integer> sudahDipilih = new LinkedHashSet<>();
        for (String id : form.featureIds()) {
            Feature feature = fiturAktif(id, Feature.class).orElseThrow(() -> new ValidationException("estimate.error.form"));
            if (!feature.getKategori().equals("CORE") && !feature.getKategori().equals("INTEGRATION")) {
                throw new ValidationException("estimate.error.form");
            }
            if (sudahDipilih.add(feature.getId())) {
                fitur.add(feature);
            }
        }

        String nama = wajib(form.nama(), 100);
        String instansi = opsional(form.instansi(), 100);
        String email = wajib(form.email(), 100);
        if (!EMAIL.matcher(email).matches()) {
            throw new ValidationException("validation.email");
        }
        String noWa = wajib(form.noWa(), 20);
        if (!NO_WA.matcher(noWa).matches()) {
            throw new ValidationException("validation.whatsapp");
        }
        String catatan = opsional(form.notes(), MAKS_CATATAN);

        Quotation quotation = calculator.hitung(platform, database, fitur);
        Client client = new Client(0, nama, instansi, email, noWa);
        ProjectRequest request = new ProjectRequest(0, buatKodeTiket(), client, quotation.getRincian(), catatan,
                quotation.getSubtotal(), quotation.getDiskon(), quotation.getTotal(), RequestStatus.PENDING,
                null, null, LocalDateTime.now());
        requestDAO.insert(request);
        return request.getKodeTiket();
    }

    /**
     * Jalankan aksi admin: "review", "approve" (nilai = link grup WA), atau "reject" (nilai = alasan).
     * Aturan statusnya ada di ProjectRequest, di sini hanya memanggil dan menyimpan hasilnya
     * beserta catatan riwayatnya (siapa dan kapan).
     *
     * @return false kalau pengajuan tidak ditemukan
     */
    public boolean ubahStatus(int id, String aksi, String nilai, Admin admin)
            throws InvalidStatusException, ValidationException {
        Optional<ProjectRequest> found = requestDAO.findById(id);
        if (found.isEmpty()) {
            return false;
        }
        ProjectRequest request = found.get();
        RequestStatus statusLama = request.getStatus();

        switch (aksi == null ? "" : aksi) {
            case "review" -> request.mulaiReview();
            case "approve" -> request.approve(nilai);
            case "reject" -> request.reject(nilai);
            default -> throw new ValidationException("validation.invalid");
        }

        if (!requestDAO.updateStatus(request, statusLama, admin.getId())) {
            // Status di database sudah diubah orang lain sejak halaman ini dibuka
            throw new InvalidStatusException("Status pengajuan " + id + " sudah berubah");
        }
        return true;
    }

    private String buatKodeTiket() {
        for (int i = 0; i < MAKS_PERCOBAAN_TIKET; i++) {
            String kode = TicketGenerator.buat();
            if (!requestDAO.ticketExists(kode)) {
                return kode;
            }
        }
        throw new IllegalStateException("Gagal membuat kode tiket yang unik");
    }

    /** Fitur dengan id tersebut, kalau ada, aktif, dan jenisnya sesuai. */
    private <T extends Feature> Optional<T> fiturAktif(String id, Class<T> jenis) {
        try {
            return featureDAO.findById(Integer.parseInt(id == null ? "" : id.trim()))
                    .filter(Feature::isAktif)
                    .filter(jenis::isInstance)
                    .map(jenis::cast);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private static String wajib(String value, int maks) throws ValidationException {
        String bersih = value == null ? "" : value.trim();
        if (bersih.isEmpty() || bersih.length() > maks) {
            throw new ValidationException("estimate.error.form");
        }
        return bersih;
    }

    private static String opsional(String value, int maks) throws ValidationException {
        String bersih = value == null ? "" : value.trim();
        if (bersih.length() > maks) {
            throw new ValidationException("estimate.error.form");
        }
        return bersih.isEmpty() ? null : bersih;
    }
}
