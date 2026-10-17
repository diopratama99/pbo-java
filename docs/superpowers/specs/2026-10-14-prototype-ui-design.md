# Spec: Prototype UI "Bisa."

**Tanggal:** 2026-10-14
**Branch:** `feature/prototype-ui`
**Referensi:** `plan.md` (rencana proyek lengkap)

---

## 1. Tujuan

Membuat prototype UI untuk semua halaman di `plan.md`, **tanpa koneksi ke database**.

- Prototype ini **dipakai lanjut**, bukan dibuang. Halaman dibuat langsung sebagai Servlet + JSP. Saat masuk tahap database, servlet dan JSP tidak perlu ditulis ulang, cukup sumber datanya yang diganti.
- Fokusnya adalah **tampilan dan alur**. Logika bisnis (perhitungan harga di server, aturan status, pembuatan kode tiket) dikerjakan di tahap berikutnya.
- Desain dan copywriting dibuat **minim noise**, supaya user fokus ke tujuannya: melihat estimasi harga, melihat portofolio, dan cek pengajuan.
- Semua halaman bisa **switch bahasa Indonesia dan Inggris**.
- Tampilan di **HP dan laptop sama penting**.

## 2. Ringkasan keputusan

| Topik | Keputusan |
|---|---|
| Nama aplikasi | **"Bisa."** (sementara, mudah diganti karena hanya ditulis di file terjemahan) |
| CSS | **Tailwind v4** lewat npm, menggantikan Bootstrap di `plan.md` |
| Hasil build CSS | Ikut di-commit, supaya project bisa dijalankan tanpa Node |
| Bahasa | ID (default) dan EN, termasuk data fitur dan portofolio |
| Sumber data | Data dummy di memori, lewat interface DAO yang nanti diganti ke versi database |
| Logika bisnis | Ditunda. Preview harga hanya di JavaScript |
| Device | HP (375px) dan laptop (1440px) sama penting |
| Halaman admin | Style terang yang sama, tanpa blob dan grain |
| Test | **Tidak ada test sama sekali** di tahap prototype |
| Implementation plan | **Tidak dibuat.** Pengerjaan langsung mengikuti urutan di bagian 8 |

## 3. Ruang lingkup

**Termasuk:**
- 10 halaman (lihat bagian 8), termasuk halaman profil tim.
- Layout klien dan layout admin.
- Switch bahasa ID/EN.
- Data dummy untuk portofolio, fitur, dan pengajuan.
- Preview harga di kalkulator dengan JavaScript.

**Tidak termasuk (ditunda ke tahap berikutnya):**
- Koneksi database dan perubahan `schema.sql`.
- `QuotationCalculator`, `PricingRule`, dan perhitungan ulang harga di server.
- Aturan perubahan status (`approve()`, `reject()`, dll) dan `InvalidStatusException`.
- Pembuatan kode tiket sungguhan.
- Hashing password (`PasswordUtil`) dan filter penjaga `/admin/*`.
- Penyimpanan data apa pun (tambah, ubah, hapus).
- Unit test.

`TesServlet` dan `tes.jsp` tidak diubah. Keduanya tetap dipakai untuk mengecek koneksi database nanti.

## 4. Struktur folder dan URL

### Struktur folder

```
pbo-java/
├── package.json                      # Tailwind v4, script "dev" (watch) dan "build" (minify)
├── src/main/
│   ├── tailwind/app.css              # sumber Tailwind: @import, @theme, komponen
│   ├── resources/i18n/
│   │   ├── messages_id.properties    # semua teks UI bahasa Indonesia
│   │   └── messages_en.properties    # semua teks UI bahasa Inggris
│   ├── java/com/pbojava/
│   │   ├── controller/               # 1 servlet per halaman
│   │   ├── model/                    # model dari plan.md + LocalizedText
│   │   ├── dao/                      # interface DAO + DAOFactory
│   │   ├── dao/memory/               # implementasi data dummy
│   │   └── util/LanguageFilter.java
│   └── webapp/
│       ├── assets/
│       │   ├── css/app.css           # HASIL build Tailwind (di-commit)
│       │   ├── fonts/                # font Outfit dan Reenie Beanie di-host sendiri
│       │   └── js/                   # vanilla JavaScript
│       └── WEB-INF/
│           ├── tags/layout.tag       # layout halaman klien
│           ├── tags/admin-layout.tag # layout halaman admin
│           └── views/
│               ├── client/
│               └── admin/
```

- JSP disimpan di dalam `WEB-INF`, jadi tidak bisa dibuka langsung dari browser. Setiap halaman selalu lewat servlet dulu (pola MVC, sama seperti `TesServlet`).
- Layout memakai **JSP tag file**. Kerangka HTML (`<head>`, navbar, footer) ditulis sekali di `layout.tag`, lalu isi tiap halaman disisipkan lewat `<jsp:doBody/>`.
- `node_modules/` ditambahkan ke `.gitignore`.

### Daftar URL

URL memakai bahasa Inggris dan **tidak berubah saat bahasa diganti**.

| URL | Method | Servlet | JSP |
|---|---|---|---|
| `/` | GET | `HomeServlet` | `client/home.jsp` |
| `/team` | GET | `TeamServlet` | `client/team.jsp` |
| `/estimate` | GET, POST | `EstimateServlet` | `client/estimate.jsp` |
| `/estimate/success` | GET | `EstimateServlet` | `client/success.jsp` |
| `/track` | GET, POST | `TrackServlet` | `client/track.jsp` |
| `/admin`, `/admin/login`, `/admin/logout` | GET, POST | `AdminLoginServlet` | `admin/login.jsp` |
| `/admin/requests` | GET, POST | `AdminRequestServlet` | `admin/requests.jsp`, `admin/request-detail.jsp` |
| `/admin/portfolios` | GET, POST | `AdminPortfolioServlet` | `admin/portfolios.jsp` |
| `/admin/features` | GET, POST | `AdminFeatureServlet` | `admin/features.jsp` |

## 5. Switch bahasa

### Cara kerja

`LanguageFilter` berjalan sebelum semua servlet dan menentukan bahasa dengan urutan prioritas berikut:

| Prioritas | Sumber | Kapan terjadi |
|---|---|---|
| 1 | Parameter URL `?lang=id` atau `?lang=en` | User baru saja klik tombol ID/EN |
| 2 | Cookie `lang` | User pernah memilih bahasa sebelumnya |
| 3 | Default `id` | Pengunjung baru |

- Hanya `id` dan `en` yang diterima. Nilai lain diabaikan.
- Bahasa yang terpilih disimpan di session (`sessionScope.lang`), lalu dipakai sebagai locale JSTL untuk request tersebut.
- Kalau bahasa datang dari parameter URL, filter menyimpan cookie `lang` (berlaku 1 tahun), lalu **redirect ke URL yang sama tanpa `?lang=`**.
- Filter tidak memproses request ke `/assets/*`.
- Tag `<html lang="...">` di layout mengikuti bahasa yang aktif.
- Tombol pill **ID / EN** ada di navbar klien dan admin.

### Teks UI

- Semua teks UI ditulis di `messages_id.properties` dan `messages_en.properties`, lalu dipanggil di JSP dengan `<fmt:message key="..."/>`.
- Format key: `halaman.bagian.elemen`, contohnya `estimate.form.email`. Teks yang dipakai bersama memakai prefix `common.` atau `app.`.
- Nama aplikasi hanya ditulis di key `app.name`.
- Kalau sebuah key belum ada, halaman menampilkan `???key???`, jadi mudah terlihat saat dicek.

### Perubahan `web.xml`

- Hapus context-param `jakarta.servlet.jsp.jstl.fmt.locale` (nilainya sekarang `id-ID`), karena setting ini memaksa semua halaman jadi bahasa Indonesia.
- Tambah context-param `jakarta.servlet.jsp.jstl.fmt.localizationContext` dengan nilai `i18n.messages`, supaya JSP tidak perlu menulis `<fmt:setBundle>`.

### Teks dari data

Nama dan deskripsi fitur, serta judul dan deskripsi portofolio, juga dibuat **dua bahasa**.

- Model memakai class kecil `LocalizedText` yang menyimpan versi `id` dan `en`. JSP memanggilnya dengan `${fitur.nama.get(sessionScope.lang)}`.
- Form admin menampilkan 2 input (ID dan EN) untuk setiap teks yang diterjemahkan.
- Penambahan kolom `_en` di `schema.sql` dikerjakan di tahap database.

### Format harga

Mata uang selalu Rupiah. Hanya pemisah ribuannya yang mengikuti bahasa:
- ID: `Rp 14.350.000`
- EN: `Rp 14,350,000`

## 6. Data dummy

### Struktur

```
Servlet ──► DAOFactory ──► FeatureDAO (interface)
                                └── InMemoryFeatureDAO   (sekarang)
                                └── JdbcFeatureDAO       (tahap database)
```

- Servlet hanya mengambil data lewat `DAOFactory`. Untuk pindah ke database, cukup ubah baris di `DAOFactory`.
- Interface DAO **hanya berisi method baca** yang benar-benar dipakai halaman saat ini, misalnya `findAll()`, `findActive()`, atau `findByTicketAndEmail()`. Method tambah, ubah, dan hapus ditambahkan di tahap database.
- Perbedaan dengan `plan.md`: `FeatureDAO`, `PortfolioDAO`, dll dijadikan interface, dan implementasinya ada di class terpisah.
- Model dan data dummy **dibuat saat halaman pertama yang memakainya dikerjakan**, tidak dibuat di awal.
- Data dummy fitur mengikuti isi `seed.sql`, ditambah versi bahasa Inggrisnya.
- Data hilang dan kembali ke kondisi awal setiap kali Jetty restart atau reload. Untuk prototype, ini tidak masalah.

### Perilaku tiap halaman

| Halaman | Perilaku di prototype |
|---|---|
| Landing | Menampilkan portofolio dari data dummy |
| Estimasi | Menampilkan fitur aktif dari data dummy. Harga dihitung langsung di JavaScript mengikuti aturan di `plan.md` bagian 6 (pengali platform, biaya setup integrasi, diskon bundling 10% untuk 5 fitur atau lebih). Data harga dikirim ke JavaScript lewat atribut `data-*` di HTML. Validasi form cukup memakai atribut HTML seperti `required` dan `type="email"` |
| Submit pengajuan | POST ke `/estimate`, lalu servlet langsung redirect ke `/estimate/success?ticket=DA-7K2P9Q`. Tidak ada data yang disimpan |
| Pengajuan berhasil | Menampilkan kode tiket dari parameter URL |
| Cek status | POST tiket + email, lalu dicari di data dummy. Hasilnya tampil di halaman yang sama. Kalau tidak cocok, tampil pesan "tidak ditemukan" |
| Login admin | Akun dummy `admin` / `admin123`, dicek dengan perbandingan biasa. Kalau berhasil, admin disimpan di session lalu diarahkan ke `/admin/requests` |
| Aksi admin (simpan, hapus, review, setujui, tolak) | POST lalu redirect kembali ke halaman sebelumnya. Data tidak berubah, dan tidak ada notifikasi |

### Tiket contoh untuk cek status

Keempat tiket memakai email `klien@contoh.com`.

| Kode tiket | Status | Yang tampil |
|---|---|---|
| `DA-PEND01` | PENDING | Status menunggu |
| `DA-REVW01` | REVIEW | Status sedang direview |
| `DA-APPR01` | APPROVED | Tombol "Gabung Grup WhatsApp" |
| `DA-REJT01` | REJECTED | Alasan penolakan |

## 7. Design system

Style awal (Hyper-Saturated Fluid, kuning dan hitam) diganti pada 2026-10-17 karena kontrasnya terlalu tinggi dan bikin mata lelah. Style sekarang adalah **Digital Wellness**: background krem, warna pastel, blob blur di background, dan grain halus seperti kertas. Warna peach dari referensi diganti dengan **glacier teal** supaya tidak terkesan terlalu girly.

### Design token (`src/main/tailwind/app.css`)

| Token | Nilai | Kegunaan |
|---|---|---|
| `--color-cream` | `#FDFCF8` | Background utama |
| `--color-sage` | `#E8EFE8` | Blob, section "Cara kerja", hover menu |
| `--color-lavender` | `#EFEDF4` | Blob, badge sekunder, badge status REVIEW |
| `--color-glacier` | `#A8D5D2` | Aksen utama: kartu fitur terpilih, badge APPROVED, menu admin aktif |
| `--color-glacier-deep` | `#3E7C78` | Versi gelap glacier untuk teks dan titik kecil, supaya tetap terbaca |
| `--color-ink` | `#292524` | Teks utama dan tombol utama |
| `--color-muted` | `#78716C` | Teks sekunder dan label |
| `--color-danger` | `#B4463F` | Status REJECTED, tombol tolak/hapus, pesan error |
| `--font-sans` | Outfit (di-host sendiri) | Semua teks |
| `--font-hand` | Reenie Beanie (di-host sendiri) | Teks tulisan tangan untuk penekanan |
| `--radius-card` | `2rem` | Kartu, panel, input textarea |
| `--radius-section` | `3rem` | Section besar seperti "Cara kerja" |
| `--radius-blob` | `4rem` | Sudut foto tim |
| `--shadow-soft` | `0 4px 20px -2px rgba(0,0,0,0.05)` | Semua kartu |

### Aturan umum

- Semua teks memakai sentence case. Tidak ada label uppercase.
- Judul hero 48px di HP sampai 96px di laptop, `tracking-tight`.
- Setiap hero halaman klien boleh punya satu baris tulisan tangan (Reenie Beanie, warna glacier-deep) di bawah judul. Teksnya ada di key `*.hero.accent`.
- Radius semua container antara 2rem dan 4rem. Tombol dan badge berbentuk pill.
- Grain: lapisan noise SVG dengan opacity 0.35 di atas seluruh halaman klien (class `grain` di `<body>`).
- Blob: 3 lingkaran blur (sage, lavender, glacier) di belakang bagian atas halaman klien, melayang naik turun 10px dalam 6 detik.
- Reveal on scroll: elemen `data-rise` naik dari 30px ke 0 dan muncul dari opacity 0 ke 1 dalam 0.8 detik.
- Saat *reduce motion* aktif, animasi float dimatikan, dan elemen langsung tampil.

### Komponen dasar

| Komponen | Class | Style |
|---|---|---|
| Tombol utama | `.btn .btn-primary` | Pill `bg-ink`, teks krem |
| Tombol aksen | `.btn .btn-accent` | Pill `bg-glacier`, teks ink |
| Tombol outline | `.btn .btn-outline` | Pill border tipis |
| Tombol bahaya | `.btn .btn-danger` | Pill border merah tipis |
| Kartu | `.card` | Putih, radius 2rem, shadow lembut |
| Kartu kaca | `.glass` | Putih 60% + blur, dipakai di atas blob |
| Panel admin | `.panel` | `.card` + padding |
| Badge | `.badge` | Pill kecil, sentence case |
| Input | `.input`, `.textarea` | Putih, border tipis, ring glacier saat focus |

### Badge status pengajuan

| Status | Style |
|---|---|
| PENDING | Abu-abu muda (`bg-ink/5`) |
| REVIEW | Lavender |
| APPROVED | Glacier |
| REJECTED | Merah muda (`bg-danger/10`) dengan teks merah |

### Halaman admin

Admin memakai style terang yang sama, tetapi tanpa blob dan grain supaya tabel dan form tetap mudah dibaca. Sidebar dan semua isi memakai `.panel`, dan menu yang aktif berwarna glacier.

### Konten jujur

- Trust badge hanya berisi klaim yang benar dari produk ini, yaitu "Estimasi gratis" dan "Harga transparan".

## 8. Urutan pengerjaan

| Langkah | Halaman | Catatan |
|---|---|---|
| 1 | Landing + portofolio | Sekaligus fondasi: Tailwind, font, `layout.tag`, navbar, `LanguageFilter`, file terjemahan, perubahan `web.xml` |
| 2 | Estimasi (kalkulator + form) | Preview harga JavaScript |
| 3 | Pengajuan berhasil | Menampilkan kode tiket |
| 4 | Cek status | 4 tiket contoh |
| 5 | Login admin | Sekaligus `admin-layout.tag` |
| 6 | Daftar pengajuan | Tabel + filter status |
| 7 | Detail pengajuan | Tombol Review, Setujui, Tolak (tanpa menyimpan) |
| 8 | Kelola portofolio | Form 2 bahasa |
| 9 | Kelola fitur & harga | Form 2 bahasa |
| 10 | Profil tim | Foto PNG tanpa background, nama, posisi, pendidikan, pengalaman |

### Alur kerja setiap halaman

1. Kerjakan halamannya, termasuk isi dan copywriting ID + EN. Tidak perlu usulan copywriting terlebih dahulu.
2. Cek manual: `mvnw compile` berhasil, halaman terbuka di Jetty, tampilan di lebar 375px dan 1440px, serta tampilan dalam bahasa ID dan EN.
3. User review langsung di browser, termasuk isi dan copywriting.
4. **User yang melakukan commit.** Claude tidak melakukan commit.

Langkah 1 dikerjakan dan direview sendiri. Atas permintaan user, langkah 2 sampai 10 dikerjakan sekaligus, lalu direview bersama.

### Cara menjalankan saat develop

```
npm run dev              # terminal 1: Tailwind watch
.\mvnw.cmd jetty:run     # terminal 2: server, buka http://localhost:8080/pbo-java/
```

## 9. Hal yang dicek setelah prototype jadi

- Design system secara keseluruhan (warna, posisi glass card, warna status). Disetujui sementara dan akan dinilai ulang dari tampilan asli.
- Nama aplikasi "Bisa." masih sementara.
- Switch bahasa me-reload halaman. Pilihan di kalkulator disimpan di `sessionStorage`, jadi tidak hilang. Hasil cek status tetap hilang dan user perlu cek ulang.
- Gambar portofolio memakai ilustrasi SVG lokal di `assets/img/portfolio/`, jadi tetap tampil tanpa internet.
- Data tim (pendidikan, posisi, pengalaman) masih contoh di `InMemoryTeamDAO`. Foto disimpan sebagai `assets/img/team/<nama>.png`. Selama file belum ada, tampil siluet placeholder.
