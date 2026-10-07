# Rencana Proyek Tugas Akhir PBO
**Judul:** Portal Portfolio & Pengajuan Proyek "Dio & Agmar Software House"
**Mata Kuliah:** Pemrograman Berorientasi Objek (Java)
**Anggota:** Dio, Agmar
**Versi:** 2.0 (revisi dari rancangan awal)

---

## 1. Gambaran Singkat
Aplikasi web untuk software house kecil kami, "Dio & Agmar". Isinya dua bagian:
1. **Etalase portofolio**, yaitu daftar proyek yang pernah kami kerjakan.
2. **Kalkulator estimasi + pengajuan proyek.** Calon klien memilih platform, database, dan fitur yang dibutuhkan, lalu sistem menghitung estimasi harganya. Kalau cocok, klien mengirim pengajuan dan mendapat kode tiket untuk memantau statusnya. Admin me-review pengajuan tersebut. Kalau disetujui, admin memberikan link grup WhatsApp untuk diskusi lanjutan.

Fokus utama proyek ini adalah **logika di sisi Java**: perhitungan harga, aturan perubahan status, dan akses database. Tampilan dibuat secukupnya pakai Bootstrap.

## 2. Latar Belakang
Masalah yang ingin diselesaikan:
- Banyak calon klien bertanya harga dulu, lalu mundur karena budget tidak cukup. Waktu habis untuk menjelaskan hal yang sama berulang kali.
- Klien awam sering bingung soal istilah teknis (platform, database, integrasi).
- Permintaan proyek masuk lewat chat pribadi dan tidak tercatat di satu tempat.

Solusinya: klien bisa menghitung estimasi sendiri, dan semua pengajuan tercatat di database dengan status yang jelas.

## 3. Pengguna
| Role | Login? | Yang bisa dilakukan |
|---|---|---|
| **Klien** | Tidak | Melihat portofolio, menghitung estimasi, mengirim pengajuan, cek status pakai kode tiket + email |
| **Admin** | Ya | Kelola portofolio, kelola daftar fitur & harga, review pengajuan |

---

## 4. Fitur

### A. Sisi Klien
1. **Landing page & portofolio**
   - Profil singkat Dio & Agmar.
   - Daftar proyek: judul, gambar (berupa URL), deskripsi, tech stack.
2. **Kalkulator estimasi**
   - Pilih 1 platform (radio), 1 database (radio), dan fitur utama/integrasi (checkbox).
   - Harga di layar langsung berubah saat opsi dipilih (JavaScript, **hanya sebagai preview**).
   - Kolom catatan bebas untuk kebutuhan khusus.
3. **Pengajuan**
   - Isi nama, instansi, nomor WA, dan email, lalu submit.
   - **Server menghitung ulang harga di Java.** Harga yang disimpan adalah hasil hitungan server, bukan angka dari browser, karena angka dari browser bisa diubah lewat inspect element.
   - Setelah submit, klien mendapat **kode tiket** (contoh: `DA-7K2P9Q`).
4. **Cek status**
   - Masukkan kode tiket + email (dua-duanya harus cocok).
   - Status `APPROVED` memunculkan tombol "Gabung Grup WhatsApp".
   - Status `REJECTED` menampilkan alasan penolakan.

### B. Sisi Admin
1. **Login/logout.** Pakai session. Halaman `/admin/*` dijaga oleh filter.
2. **Kelola portofolio (CRUD).**
3. **Kelola fitur & harga (CRUD).**
   - Admin bisa mengubah harga tanpa mengubah kode.
   - Fitur yang sudah pernah dipakai di pengajuan **tidak dihapus**, tapi dinonaktifkan (`is_active = false`) agar data lama tidak rusak.
4. **Kelola pengajuan**
   - Tabel pengajuan dengan filter berdasarkan status.
   - Halaman detail: data klien, fitur yang dipilih beserta harganya saat itu, catatan, dan total.
   - Tombol aksi sesuai status: Mulai Review / Setujui (wajib isi link WAG) / Tolak (wajib isi alasan).

---

## 5. Alur Status Pengajuan

```
PENDING ──► REVIEW ──► APPROVED
   │           │
   └───────────┴──────► REJECTED
```

- `PENDING`: baru masuk, belum dibuka admin.
- `REVIEW`: sedang dipelajari admin.
- `APPROVED`: disetujui dan link WAG wajib diisi (harus diawali `https://chat.whatsapp.com/`).
- `REJECTED`: ditolak dan alasan wajib diisi. Boleh langsung dari `PENDING` (misalnya pengajuan spam).
- `APPROVED` dan `REJECTED` adalah status akhir dan tidak bisa diubah lagi.

Aturan ini **ditulis di class `ProjectRequest`**, bukan di servlet. Jadi dari mana pun method-nya dipanggil, aturannya tetap berlaku. Kalau ada perubahan status yang tidak valid, method melempar `InvalidStatusException`.

---

## 6. Aturan Perhitungan Harga

Setiap jenis fitur punya cara hitung sendiri. Di bagian inilah polymorphism dipakai.

| Kategori | Cara hitung | Contoh |
|---|---|---|
| **Platform** | Harga dasar. Punya *pengali* yang memengaruhi fitur utama | Web (×1.0), Mobile (×1.3), Web + Mobile (×1.6) |
| **Database** | Harga tetap | MySQL, PostgreSQL |
| **Fitur Utama** | Harga dasar × pengali platform | Login, Chat, Dashboard |
| **Integrasi** | Harga dasar + biaya setup (tidak terpengaruh platform) | Payment Gateway, Maps API |

**Diskon bundling:** memilih ≥ 5 fitur (utama + integrasi) memberi diskon 10% dari subtotal.

**Contoh hitungan:**
| Pilihan | Hitungan | Harga |
|---|---|---|
| Platform Mobile | 5.000.000 | 5.000.000 |
| Database MySQL | 1.000.000 | 1.000.000 |
| Login | 1.500.000 × 1.3 | 1.950.000 |
| Chat | 3.000.000 × 1.3 | 3.900.000 |
| Payment Gateway | 2.000.000 + 500.000 | 2.500.000 |
| **Total** (3 fitur, belum dapat diskon) | | **14.350.000** |

Semua nilai uang memakai `BigDecimal`, bukan `double`, supaya tidak ada error pembulatan.

---

## 7. Teknologi
- **Bahasa:** Java 17
- **Web:** Jakarta Servlet + JSP + JSTL, **tanpa framework** (tanpa Spring/Hibernate)
- **Server:** Apache Tomcat 10.1 (memakai package `jakarta.*`, bukan `javax.*`)
- **Database:** MySQL 8 dengan JDBC manual (`PreparedStatement`)
- **Build:** Maven
- **Tampilan:** Bootstrap 5 + Vanilla JavaScript
- **Testing:** JUnit 5 untuk menguji logika harga dan status

Alasan tidak memakai framework: kami ingin semua class (model, DAO, service, controller) ditulis sendiri, supaya penerapan konsep OOP terlihat jelas dan bisa dijelaskan baris per baris.

---

## 8. Rancangan OOP

### Struktur package
```
com.pbojava
├── model/        User, Admin, Client, Portfolio, ProjectRequest, RequestStatus (enum)
│   └── feature/  Feature, PlatformFeature, DatabaseFeature, CoreFeature, IntegrationFeature
├── pricing/      PricingRule, BundleDiscountRule, QuotationCalculator, Quotation
├── dao/          GenericDAO<T>, AdminDAO, ClientDAO, PortfolioDAO, FeatureDAO, ProjectRequestDAO
├── service/      AuthService, QuotationService, RequestService
├── controller/   Servlet-servlet (HomeServlet, QuotationServlet, TrackingServlet, Admin...Servlet)
├── exception/    InvalidStatusException, ValidationException
└── util/         DBConnection, PasswordUtil, TicketGenerator, AdminAuthFilter
```

### Penerapan konsep OOP
| Konsep | Di mana | Penjelasan |
|---|---|---|
| **Encapsulation** | Semua model, terutama `ProjectRequest` | Field `private`. Status **tidak punya setter**, hanya bisa diubah lewat `mulaiReview()`, `approve(link)`, `reject(alasan)` |
| **Inheritance** | `User` → `Admin`, `Client` | Atribut bersama (id, nama, email) ada di `User` |
| **Inheritance** | `Feature` → 4 subclass | Lihat bagian 6 |
| **Abstraction** | `abstract class Feature`, `abstract class User` | Tidak bisa dibuat object-nya langsung |
| **Polymorphism** | `feature.hitungHarga(platform)` | `QuotationCalculator` cukup melakukan loop `List<Feature>` tanpa peduli jenis fiturnya |
| **Interface** | `PricingRule`, `GenericDAO<T>` | Aturan diskon baru cukup membuat class baru tanpa mengubah kalkulator |
| **Generics** | `GenericDAO<T>` | Method CRUD seragam untuk semua DAO |
| **Exception** | `InvalidStatusException extends Exception` | Exception buatan sendiri untuk aturan bisnis |
| **Enum** | `RequestStatus` | Nilai status pasti valid |

### Gambaran class utama
```java
public abstract class Feature {
    private int id;
    private String nama;
    private BigDecimal hargaDasar;
    private boolean aktif;

    public abstract BigDecimal hitungHarga(PlatformFeature platform);
    public abstract String getKategori();
}

public class CoreFeature extends Feature {
    @Override
    public BigDecimal hitungHarga(PlatformFeature platform) {
        return getHargaDasar().multiply(platform.getPengali());
    }
}

public class IntegrationFeature extends Feature {
    private BigDecimal biayaSetup;

    @Override
    public BigDecimal hitungHarga(PlatformFeature platform) {
        return getHargaDasar().add(biayaSetup); // tidak terpengaruh platform
    }
}

public interface PricingRule {
    BigDecimal hitungDiskon(BigDecimal subtotal, List<Feature> fiturDipilih);
}

public class ProjectRequest {
    private RequestStatus status = RequestStatus.PENDING;

    public void approve(String wagLink) throws InvalidStatusException {
        if (status != RequestStatus.REVIEW) {
            throw new InvalidStatusException("Hanya pengajuan berstatus REVIEW yang bisa di-approve");
        }
        // validasi link, lalu ubah status
    }
}
```

Saat membaca data dari tabel `master_features`, `FeatureDAO` membuat object subclass yang sesuai berdasarkan kolom `kategori`.

### Alur MVC (contoh: submit pengajuan)
```
JSP form ─► QuotationServlet ─► QuotationService ─► QuotationCalculator (hitung ulang)
                                      │
                                      └─► ClientDAO + ProjectRequestDAO ─► MySQL
         ◄── redirect ke halaman sukses (tampilkan kode tiket)
```
Servlet hanya menerima request dan meneruskan ke service. JSP hanya menampilkan data (pakai JSTL, tanpa scriptlet `<% %>`).

---

## 9. Rancangan Database

```sql
CREATE TABLE admins (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    nama VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,   -- SHA-256 + salt, tidak disimpan plain text
    salt VARCHAR(64) NOT NULL
);

-- Klien yang mengajukan lagi dengan email sama memakai baris yang sama
CREATE TABLE clients (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nama VARCHAR(100) NOT NULL,
    instansi VARCHAR(100),
    email VARCHAR(100) NOT NULL UNIQUE,
    no_wa VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE portfolios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    judul VARCHAR(150) NOT NULL,
    deskripsi TEXT,
    tech_stack VARCHAR(255),
    image_url VARCHAR(255)
);

CREATE TABLE master_features (
    id INT PRIMARY KEY AUTO_INCREMENT,
    kategori ENUM('PLATFORM', 'DATABASE', 'CORE', 'INTEGRATION') NOT NULL,
    nama_fitur VARCHAR(100) NOT NULL,
    deskripsi VARCHAR(255),                -- penjelasan singkat untuk klien awam
    harga_dasar DECIMAL(15,2) NOT NULL,
    pengali DECIMAL(4,2),                  -- hanya untuk PLATFORM
    biaya_setup DECIMAL(15,2),             -- hanya untuk INTEGRATION
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE project_requests (
    id INT PRIMARY KEY AUTO_INCREMENT,
    kode_tiket VARCHAR(12) NOT NULL UNIQUE,
    client_id INT NOT NULL,
    custom_notes TEXT,
    subtotal DECIMAL(15,2) NOT NULL,
    diskon DECIMAL(15,2) DEFAULT 0,
    total_estimasi DECIMAL(15,2) NOT NULL,
    status ENUM('PENDING', 'REVIEW', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    wag_link VARCHAR(255),
    alasan_ditolak VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES clients(id)
);

-- harga_saat_itu menyimpan harga ketika pengajuan dibuat,
-- supaya detail pengajuan lama tidak ikut berubah kalau admin mengganti harga
CREATE TABLE request_features (
    request_id INT,
    feature_id INT,
    harga_saat_itu DECIMAL(15,2) NOT NULL,
    PRIMARY KEY (request_id, feature_id),
    FOREIGN KEY (request_id) REFERENCES project_requests(id),
    FOREIGN KEY (feature_id) REFERENCES master_features(id)
);
```

Penyimpanan pengajuan (insert ke `project_requests` + `request_features`) dijalankan dalam satu **transaksi JDBC** (`setAutoCommit(false)` → `commit()` / `rollback()`), supaya tidak ada pengajuan yang tersimpan setengah.

---

## 10. Pembagian Tahapan

**Minggu 1: Fondasi**
- Setup project Maven + Tomcat + MySQL, buat script SQL + data contoh (seed).
- `DBConnection`, semua class model, hierarki `Feature`.
- `QuotationCalculator` + `BundleDiscountRule` + unit test JUnit.
- Login admin + filter.

**Minggu 2: Admin & Kalkulator**
- CRUD portofolio dan CRUD fitur di halaman admin.
- Landing page + halaman portofolio.
- Halaman kalkulator (preview JS) + submit (hitung ulang di server) + kode tiket.

**Minggu 3: Pengajuan & Finalisasi**
- Dashboard pengajuan admin + aksi review/approve/reject.
- Halaman cek status untuk klien.
- Uji coba alur dari awal sampai akhir dan perbaikan bug.
- Finalisasi class diagram, use case diagram, dan laporan.

---

## 11. Di Luar Cakupan
Hal-hal berikut sengaja **tidak** dikerjakan supaya waktu fokus ke inti proyek:
- Upload file gambar (portofolio cukup memakai URL gambar).
- Notifikasi email otomatis.
- Pembuatan grup WhatsApp otomatis (tidak ada API resmi, jadi admin membuat grup secara manual lalu menempelkan link-nya).
- Pembayaran online.
- Lebih dari satu level hak akses admin.

## 12. Hasil yang Dikumpulkan
- Source code (Maven project)
- Script SQL (struktur tabel + data contoh)
- Class diagram dan use case diagram
- Laporan
- Demo aplikasi

## 13. Perlu Dikonfirmasi ke Dosen
- Apakah aplikasi web (Servlet/JSP) diperbolehkan, atau harus desktop?
- Diagram UML apa saja yang wajib dilampirkan?
- Apakah unit test (JUnit) dihitung sebagai nilai tambah?
