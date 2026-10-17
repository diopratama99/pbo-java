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

## Schema database per halaman

Struktur lengkapnya ada di `db/schema.sql`. Di bawah ini tabel dan kolom yang dipakai setiap halaman setelah prototype disambungkan ke database.

### Beranda

```
portfolios (id, judul, deskripsi, tech_stack, image_url)
```

Harga "Mulai dari" berasal dari `master_features`, yaitu Website ditambah MySQL. Di prototype angkanya masih ditulis langsung.

### Tim

Tabelnya belum ada di `db/schema.sql`, dan datanya masih di `InMemoryTeamDAO`. Usulan tabelnya:

```sql
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

### Hitung estimasi

Membaca fitur yang aktif:

```
master_features (id, kategori, nama_fitur, deskripsi, harga_dasar, pengali, biaya_setup, is_active)
```

Saat pengajuan dikirim, data disimpan dalam satu transaksi:

```
clients          (nama, instansi, email, no_wa)
project_requests (kode_tiket, client_id, custom_notes, subtotal, diskon, total_estimasi, status)
request_features (request_id, feature_id, harga_saat_itu)
```

### Pengajuan terkirim

```
project_requests (kode_tiket)
```

### Cek status

Mencari pengajuan berdasarkan `kode_tiket` dan `clients.email`:

```
project_requests (kode_tiket, client_id, status, total_estimasi, wag_link, alasan_ditolak, created_at)
clients          (id, email)
```

### Login admin

```
admins (username, nama, password_hash, salt)
```

`db/seed.sql` belum berisi akun admin, karena format hash-nya harus sama dengan `PasswordUtil` di Java.

### Daftar pengajuan

```
project_requests (id, kode_tiket, client_id, total_estimasi, status, created_at)
clients          (id, nama, email)
```

### Detail pengajuan

Membaca:

```
project_requests (semua kolom)
clients          (nama, instansi, email, no_wa)
request_features (request_id, feature_id, harga_saat_itu)
master_features  (id, nama_fitur, kategori)
```

Aksi review, setujui, dan tolak mengubah:

```
project_requests (status, wag_link, alasan_ditolak, updated_at)
```

### Kelola portofolio

```
portfolios (id, judul, deskripsi, tech_stack, image_url)
```

### Kelola fitur dan harga

```
master_features (id, kategori, nama_fitur, deskripsi, harga_dasar, pengali, biaya_setup, is_active)
```

Fitur yang sudah pernah dipakai di pengajuan tidak dihapus. Kolom `is_active` diubah menjadi `FALSE`, supaya fitur itu hilang dari kalkulator tapi pengajuan lama tetap utuh.

### Kolom bahasa Inggris

Prototype sudah menampilkan nama dan deskripsi fitur serta portofolio dalam 2 bahasa, tapi `db/schema.sql` belum punya kolomnya. Di tahap database, tambahkan:

```sql
ALTER TABLE master_features
    ADD COLUMN nama_fitur_en VARCHAR(100),
    ADD COLUMN deskripsi_en VARCHAR(255);

ALTER TABLE portfolios
    ADD COLUMN judul_en VARCHAR(150),
    ADD COLUMN deskripsi_en TEXT;
```
