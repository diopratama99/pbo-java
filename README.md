# Bisa.

Portal portofolio dan pengajuan proyek untuk software house mahasiswa Dio, Agmar, dan Wulandari. Dibuat dengan Java (Servlet + JSP), Tailwind CSS, dan MySQL.

Semua data dibaca dan disimpan di MySQL lewat DAO JDBC (`dao/jdbc`). Harga pengajuan dihitung ulang di server (`QuotationCalculator`), aturan status ada di `ProjectRequest`, dan halaman admin hanya bisa dibuka setelah login.

## Cara menjalankan

Yang perlu di-install hanya JDK 17 atau lebih baru. Node.js baru dibutuhkan kalau kamu mengubah tampilan.

Aplikasi butuh database. Untuk development, pakai database di server lewat SSH tunnel:

1. Salin `src/main/resources/db.properties.example` menjadi `db.properties`, lalu isi `db.password` (sama dengan `MYSQL_PASSWORD` di `~/pbo-java-db/.env` di server).
2. Buka SSH tunnel di terminal terpisah, dan biarkan tetap jalan:
   ```powershell
   ssh -N -L 3307:127.0.0.1:3306 leykopin@192.168.1.10
   ```
3. Cek koneksinya:
   ```powershell
   .\mvnw.cmd compile exec:java "-Dexec.mainClass=com.pbojava.util.TesKoneksi"
   ```
4. Jalankan server:
   ```powershell
   .\mvnw.cmd jetty:run
   ```
5. Buka http://localhost:8080/pbo-java/

Saat pertama kali dijalankan, Maven perlu internet untuk download dependency.

Setelah mengedit file Java atau file terjemahan di `src/main/resources/i18n`, jalankan `.\mvnw.cmd compile` di terminal lain. Jetty akan memuat ulang perubahannya dalam beberapa detik. Perubahan JSP langsung terlihat saat halaman di-refresh.

Kalau kamu menambah atau mengubah class Tailwind di JSP atau JavaScript, jalankan juga:

```powershell
npm install
npm run dev
```

### Unit test

```powershell
.\mvnw.cmd test
```

Test-nya mengecek perhitungan harga (`QuotationCalculatorTest`), alur status (`ProjectRequestTest`), hash password, dan kode tiket. Test tidak butuh database.

### Database sendiri (tanpa server)

Kalau mau memakai MySQL di laptop sendiri:

1. Salin `db/.env.example` menjadi `db/.env`, lalu isi password-nya.
2. Jalankan MySQL. Tabel dan data contoh dibuat otomatis dari `db/schema.sql` dan `db/seed.sql`.
   ```powershell
   cd db
   docker compose up -d
   ```
3. Di `db.properties`, ganti port di `db.url` menjadi `3306` dan samakan `db.password` dengan `MYSQL_PASSWORD` di `db/.env`.
4. Buat akun admin (lihat bagian [Login admin](#login-admin)).

## Deploy ke server

Aplikasi berjalan di server `serverdio` (`192.168.1.10`) sebagai container `pbo-java-web` (Tomcat 10.1), di port `8088`, dan bisa diakses di https://bisa.temanlabs.tech/. Di server ini aplikasi dipasang di root, jadi alamatnya `http://serverdio:8088/`, bukan `/pbo-java/`.

Database-nya container `pbo-java-mysql` (MySQL 8.4) dari `~/pbo-java-db/docker-compose.yml`. Container web terhubung ke database lewat network `pbo-java_default`, dengan host `mysql`.

Setiap push ke branch `main` otomatis ter-deploy dalam waktu sekitar 1 menit. Cron di server menjalankan [deploy/auto-deploy.sh](deploy/auto-deploy.sh) tiap menit. Skrip itu mengecek `origin/main`. Kalau ada commit baru, kode di-pull lalu container di-build ulang. Kalau build gagal, versi lama tetap jalan.

| Yang dicari | Lokasi di server |
|---|---|
| Kode aplikasi | `~/apps/pbo-java` |
| Log deploy | `~/apps/pbo-java-deploy.log` |
| Konfigurasi database | `~/pbo-java-db/db.properties` (tidak masuk git) |
| Password akun `admin` | `~/pbo-java-db/admin-password.txt` (hanya bisa dibaca pemilik) |
| Backup database | `~/pbo-java-db/backup-*.sql` |

Perintah yang sering dipakai (jalankan di server):

```bash
tail -f ~/apps/pbo-java-deploy.log           # lihat proses deploy
docker logs -f pbo-java-web                  # lihat log Tomcat
~/apps/pbo-java/deploy/auto-deploy.sh --force   # deploy ulang tanpa menunggu commit baru
```

## Login admin

Buka http://localhost:8080/pbo-java/admin/login (atau https://bisa.temanlabs.tech/admin/login).

Username-nya `admin`. Password-nya acak dan disimpan di server, di `~/pbo-java-db/admin-password.txt`. Password disimpan di database dalam bentuk hash PBKDF2 + salt (`PasswordUtil`), tidak pernah sebagai teks biasa.

Untuk membuat akun admin baru atau mengganti password, jalankan perintah ini (dengan tunnel terbuka), lalu ketik password baru minimal 10 karakter:

```powershell
.\mvnw.cmd compile exec:java "-Dexec.mainClass=com.pbojava.util.BuatAdmin" "-Dexec.args=admin 'Admin Bisa.'"
```

Semua halaman `/admin/*` dijaga oleh `AdminAuthFilter`. Kalau belum login, pengunjung diarahkan ke halaman login.

## Halaman

Semua URL diawali `http://localhost:8080/pbo-java`. Bahasa bisa diganti lewat tombol ID / EN di navbar, dan pilihannya disimpan di cookie.

### Halaman klien

| URL | Halaman | Isi |
|---|---|---|
| `/` | Beranda | Ajakan hitung estimasi, harga termurah (platform + database termurah yang aktif), daftar portofolio, dan cara kerja |
| `/team` | Tim | Foto, posisi, pendidikan, dan pengalaman setiap anggota |
| `/estimate` | Hitung estimasi | Pilih platform, database, dan fitur. Harga di layar hanya preview dari JavaScript. Saat dikirim, server memvalidasi isian dan menghitung ulang harganya |
| `/estimate/success` | Pengajuan terkirim | Menampilkan kode tiket acak, contoh `DA-7K2P9Q` |
| `/track` | Cek status | Masukkan kode tiket dan email untuk melihat status pengajuan |

### Kode untuk tes cek status

Pakai email `klien@contoh.com` dengan salah satu kode berikut (dari `db/seed.sql`):

| Kode tiket | Status yang muncul |
|---|---|
| `DA-PEND01` | Menunggu |
| `DA-REVW01` | Direview |
| `DA-APPR01` | Disetujui, dengan tombol Gabung Grup WhatsApp |
| `DA-REJT01` | Ditolak, dengan alasan penolakan |

Kode tiket dan email harus cocok dua-duanya. Huruf besar dan kecil tidak dibedakan.

### Halaman admin

| URL | Halaman | Isi |
|---|---|---|
| `/admin/login` | Login | Masuk dengan akun admin |
| `/admin/requests` | Daftar pengajuan | Semua pengajuan, bisa difilter per status |
| `/admin/requests?id=1` | Detail pengajuan | Data klien, fitur yang dipilih beserta harganya, catatan, dan aksi sesuai status: mulai review, setujui (wajib isi link grup WhatsApp), atau tolak (wajib isi alasan) |
| `/admin/portfolios` | Kelola portofolio | Tambah, ubah, dan hapus portofolio dalam 2 bahasa |
| `/admin/features` | Kelola fitur dan harga | Tambah, ubah, dan nonaktifkan fitur. Di samping form ada preview kartu seperti yang dilihat klien di kalkulator |

## Struktur kode backend

| Package | Isi |
|---|---|
| `model`, `model/feature` | `ProjectRequest` (aturan status: `mulaiReview()`, `approve()`, `reject()`), `Feature` dan 4 subclass-nya (masing-masing punya `hitungHarga()`), `Admin`, `Client`, `Portfolio`, `TeamMember` |
| `pricing` | `QuotationCalculator`, `Quotation`, interface `PricingRule`, dan `BundleDiscountRule` (diskon 10% untuk minimal 5 fitur) |
| `service` | `RequestService`: validasi pengajuan, hitung harga, buat kode tiket, dan ubah status |
| `dao`, `dao/jdbc` | Interface DAO, `DAOFactory`, dan implementasi JDBC-nya |
| `exception` | `InvalidStatusException` dan `ValidationException` |
| `util` | `DBConnection`, `PasswordUtil`, `TicketGenerator`, `AdminAuthFilter`, `LanguageFilter`, `BuatAdmin`, `TesKoneksi` |

## Schema database

Struktur lengkapnya ada di [db/schema.sql](db/schema.sql), dan diagramnya di [db/erd.md](db/erd.md). Data contoh ada di [db/seed.sql](db/seed.sql). File seed memakai id tetap dan `ON DUPLICATE KEY UPDATE`, jadi aman dijalankan ulang tanpa membuat data dobel. Tapi menjalankannya ulang akan mengembalikan harga fitur ke angka di file itu.

### Migrasi

Database yang dibuat sebelum backend selesai belum punya kolom bahasa Inggris dan tabel Tim. Untuk database seperti itu, jalankan [db/migrations/001-backend.sql](db/migrations/001-backend.sql) lalu `db/seed.sql`. Database di server sudah dimigrasi pada 8 November 2026.

Contoh menjalankan file SQL ke database di server (dari folder project):

```bash
ssh leykopin@192.168.1.10 'docker exec -i pbo-java-mysql sh -c "mysql --default-character-set=utf8mb4 -u\"\$MYSQL_USER\" -p\"\$MYSQL_PASSWORD\" \"\$MYSQL_DATABASE\""' < db/seed.sql
```

### Tabel yang dipakai setiap halaman

| Halaman | Membaca | Mengubah |
|---|---|---|
| Beranda | `portfolios`, `master_features` (harga "Mulai dari") | |
| Tim | `team_members`, `team_experiences` | |
| Hitung estimasi | `master_features` yang `is_active = TRUE` | `clients`, `project_requests`, `request_features` dalam satu transaksi saat pengajuan dikirim |
| Pengajuan terkirim | | |
| Cek status | `project_requests` join `clients`, dicari dengan `kode_tiket` dan `clients.email` | |
| Login admin | `admins` | |
| Daftar pengajuan | `project_requests` join `clients` | |
| Detail pengajuan | `project_requests`, `clients`, `request_features` join `master_features` | `project_requests.status`, `wag_link`, `alasan_ditolak` |
| Kelola portofolio | `portfolios` | `portfolios` (tambah, ubah, hapus) |
| Kelola fitur dan harga | `master_features` | `master_features` (tambah, ubah, `is_active`) |

Fitur yang sudah pernah dipakai di pengajuan tidak dihapus. Kolom `is_active` diubah menjadi `FALSE`, supaya fitur itu hilang dari kalkulator tapi pengajuan lama tetap utuh.

Perubahan status hanya disimpan kalau status di database masih sama dengan saat halaman dibuka (`UPDATE ... WHERE status = ?`), jadi dua admin yang menekan tombol bersamaan tidak saling menimpa.
