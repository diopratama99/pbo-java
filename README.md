# Bisa.

Portal portofolio dan pengajuan proyek untuk software house mahasiswa Dio, Agmar, dan Wulandari. Dibuat dengan Java (Servlet + JSP), Tailwind CSS, dan MySQL.

Aplikasi ini masih prototype. Semua data diambil dari data dummy di Java, jadi halaman bisa dibuka tanpa database. Tombol simpan, hapus, setujui, dan tolak belum mengubah data.

## Cara menjalankan

Yang perlu di-install hanya JDK 17. Node.js baru dibutuhkan kalau kamu mengubah tampilan.

1. Jalankan server dari folder project:
   ```powershell
   .\mvnw.cmd jetty:run
   ```
2. Buka http://localhost:8080/pbo-java/

Saat pertama kali dijalankan, Maven perlu internet untuk download dependency.

Setelah mengedit file Java atau file terjemahan di `src/main/resources/i18n`, jalankan `.\mvnw.cmd compile` di terminal lain. Jetty akan memuat ulang perubahannya dalam beberapa detik. Perubahan JSP langsung terlihat saat halaman di-refresh.

Kalau kamu menambah atau mengubah class Tailwind di JSP atau JavaScript, jalankan juga:

```powershell
npm install
npm run dev
```

### Database (opsional)

Prototype belum memakai database. Database baru dipakai oleh halaman `/tes` dan nanti di tahap berikutnya.

1. Salin `db/.env.example` menjadi `db/.env`, lalu isi password-nya.
2. Jalankan MySQL. Tabel dan data contoh dibuat otomatis.
   ```powershell
   cd db
   docker compose up -d
   ```
3. Salin `src/main/resources/db.properties.example` menjadi `db.properties`. Ganti port di `db.url` menjadi `3306`, lalu samakan `db.password` dengan `MYSQL_PASSWORD` di `db/.env`.
4. Cek koneksinya:
   ```powershell
   .\mvnw.cmd compile exec:java "-Dexec.mainClass=com.pbojava.util.TesKoneksi"
   ```

## Login admin

Buka http://localhost:8080/pbo-java/admin/login

| Username | Password |
|---|---|
| `admin` | `admin123` |

Akun ini hanya ada di prototype (`InMemoryAdminDAO`). Halaman admin juga belum dikunci, jadi masih bisa dibuka tanpa login.

## Halaman

Semua URL diawali `http://localhost:8080/pbo-java`. Bahasa bisa diganti lewat tombol ID / EN di navbar, dan pilihannya disimpan di cookie.

### Halaman klien

| URL | Halaman | Isi |
|---|---|---|
| `/` | Beranda | Ajakan hitung estimasi, contoh harga termurah, daftar portofolio, dan cara kerja |
| `/team` | Tim | Foto, posisi, pendidikan, dan pengalaman setiap anggota |
| `/estimate` | Hitung estimasi | Pilih platform, database, dan fitur. Harga dihitung langsung saat opsi dipilih. Setelah itu isi data diri dan kirim pengajuan |
| `/estimate/success` | Pengajuan terkirim | Menampilkan kode tiket. Di prototype kodenya selalu `DA-7K2P9Q` |
| `/track` | Cek status | Masukkan kode tiket dan email untuk melihat status pengajuan |

### Kode untuk tes cek status

Pakai email `klien@contoh.com` dengan salah satu kode berikut:

| Kode tiket | Status yang muncul |
|---|---|
| `DA-PEND01` | Menunggu |
| `DA-REVW01` | Direview |
| `DA-APPR01` | Disetujui, dengan tombol Gabung Grup WhatsApp |
| `DA-REJT01` | Ditolak, dengan alasan penolakan |

Kode tiket dan email harus cocok dua-duanya. Kode `DA-7K2P9Q` dari halaman pengajuan terkirim tidak akan ditemukan, karena pengajuan baru belum disimpan.

### Halaman admin

| URL | Halaman | Isi |
|---|---|---|
| `/admin/login` | Login | Masuk dengan akun di atas |
| `/admin/requests` | Daftar pengajuan | Semua pengajuan, bisa difilter per status |
| `/admin/requests?id=1` | Detail pengajuan | Data klien, fitur yang dipilih beserta harganya, catatan, dan aksi sesuai status: mulai review, setujui (wajib isi link grup WhatsApp), atau tolak (wajib isi alasan) |
| `/admin/portfolios` | Kelola portofolio | Tambah, ubah, dan hapus portofolio dalam 2 bahasa |
| `/admin/features` | Kelola fitur dan harga | Tambah, ubah, dan nonaktifkan fitur. Di samping form ada preview kartu seperti yang dilihat klien di kalkulator |

## Schema database

### DDL lengkap

Isi `db/schema.sql`. File ini dijalankan otomatis oleh Docker saat database pertama kali dibuat.

```sql
SET NAMES utf8mb4;

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

### DDL tambahan yang dibutuhkan prototype

Prototype sudah menampilkan halaman Tim dan teks 2 bahasa, tapi tabel dan kolomnya belum ada di `db/schema.sql`. Ini usulan DDL untuk tahap database:

```sql
-- Teks bahasa Inggris untuk fitur dan portofolio
ALTER TABLE master_features
    ADD COLUMN nama_fitur_en VARCHAR(100),
    ADD COLUMN deskripsi_en VARCHAR(255);

ALTER TABLE portfolios
    ADD COLUMN judul_en VARCHAR(150),
    ADD COLUMN deskripsi_en TEXT;

-- Halaman Tim (datanya sekarang masih di InMemoryTeamDAO)
CREATE TABLE team_members (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nama VARCHAR(100) NOT NULL,
    foto VARCHAR(255),
    posisi VARCHAR(100) NOT NULL,
    posisi_en VARCHAR(100) NOT NULL,
    pendidikan VARCHAR(150),
    pendidikan_en VARCHAR(150),
    urutan INT DEFAULT 0
);

CREATE TABLE team_experiences (
    id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT NOT NULL,
    deskripsi VARCHAR(255) NOT NULL,
    deskripsi_en VARCHAR(255) NOT NULL,
    FOREIGN KEY (member_id) REFERENCES team_members(id)
);
```

### Tabel yang dipakai setiap halaman

| Halaman | Membaca | Mengubah |
|---|---|---|
| Beranda | `portfolios`, `master_features` (harga "Mulai dari") | |
| Tim | `team_members`, `team_experiences` | |
| Hitung estimasi | `master_features` yang `is_active = TRUE` | `clients`, `project_requests`, `request_features` dalam satu transaksi saat pengajuan dikirim |
| Pengajuan terkirim | `project_requests.kode_tiket` | |
| Cek status | `project_requests` join `clients`, dicari dengan `kode_tiket` dan `clients.email` | |
| Login admin | `admins` | |
| Daftar pengajuan | `project_requests` join `clients` | |
| Detail pengajuan | `project_requests`, `clients`, `request_features` join `master_features` | `project_requests.status`, `wag_link`, `alasan_ditolak` |
| Kelola portofolio | `portfolios` | `portfolios` (tambah, ubah, hapus) |
| Kelola fitur dan harga | `master_features` | `master_features` (tambah, ubah, `is_active`) |

Fitur yang sudah pernah dipakai di pengajuan tidak dihapus. Kolom `is_active` diubah menjadi `FALSE`, supaya fitur itu hilang dari kalkulator tapi pengajuan lama tetap utuh.

`db/seed.sql` belum berisi akun admin, karena format hash-nya harus sama dengan `PasswordUtil` di Java.
