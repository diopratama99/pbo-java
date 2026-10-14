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
| Halaman admin | Versi design yang lebih kalem |
| Test | **Tidak ada test sama sekali** di tahap prototype |
| Implementation plan | **Tidak dibuat.** Pengerjaan langsung mengikuti urutan di bagian 8 |

## 3. Ruang lingkup

**Termasuk:**
- 9 halaman (lihat bagian 8).
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
│       │   ├── fonts/inter/          # font Inter di-host sendiri
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
| `/estimate` | GET, POST | `EstimateServlet` | `client/estimate.jsp` |
| `/estimate/success` | GET | `EstimateServlet` | `client/success.jsp` |
| `/track` | GET, POST | `TrackServlet` | `client/track.jsp` |
| `/admin/login` | GET, POST | `AdminLoginServlet` | `admin/login.jsp` |
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

Mengikuti design prompt **Hyper-Saturated Fluid**, dengan 2 mode.

### Mode klien (Landing, Estimasi, Sukses, Cek Status)

- Setiap halaman dibuka dengan **hero kuning** yang berisi headline halaman dan aksi utamanya. Sudut kanan bawah `120px`, kiri bawah `40px`, dan teks hitam rata kiri.
- Konten berikutnya ada di area hitam (void).
- **Glass card diletakkan di perbatasan kuning dan hitam**, karena efek blur hanya terlihat kalau ada sesuatu di belakangnya.
- Animasi: section slide-up dengan easing liquid, glass card melayang pelan, tombol "squish" saat diklik.

### Mode admin

- Background hitam mendominasi. Kuning hanya untuk aksen: menu aktif, tombol utama, dan badge `APPROVED`.
- Tanpa blob dan tanpa animasi melayang.
- Tabel dan form ada di panel `bg-surface` dengan radius `32px`.
- Desktop first, tapi tetap bisa dipakai di HP.

### Design token (`src/main/tailwind/app.css`)

| Token | Nilai | Kegunaan |
|---|---|---|
| `--color-shout` | `#FDE047` | Kuning utama |
| `--color-void` | `#0A0A0A` | Background hitam utama |
| `--color-surface` | `#171717` | Panel gelap sekunder |
| `--color-deep` | `#262626` | Input, tabel, elemen di dalam void |
| `--font-sans` | Inter (di-host sendiri) | Semua teks |
| `--radius-glass` | `32px` | Glass card, panel, textarea |
| `--radius-liquid` | `120px` | Lengkungan hero |
| `--ease-liquid` | `cubic-bezier(0.22, 1, 0.36, 1)` | Animasi masuk |

Merah `#F87171` hanya dipakai untuk titik di badge `REJECTED` dan pesan error form.

### Komponen dasar

| Komponen | Style |
|---|---|
| Hero liquid | `bg-shout`, `rounded-br-[120px] rounded-bl-[40px]`, teks hitam rata kiri |
| Glass card | `bg-white/10 backdrop-blur-2xl border border-white/20 rounded-[32px] shadow-2xl` |
| Pill button | `rounded-full`, solid hitam atau kuning, atau outline `border-2 border-black/20`. Hover `scale-105 shadow-lg`, klik `active:scale-95` |
| Pill badge | `rounded-full`, label `text-[10px]` uppercase `tracking-widest` |
| Input | `bg-deep rounded-full`, textarea `rounded-[32px]`, border putih tipis saat focus |

### Badge status pengajuan

| Status | Style |
|---|---|
| PENDING | Pill abu-abu |
| REVIEW | Pill outline putih |
| APPROVED | Pill kuning solid |
| REJECTED | Pill gelap dengan titik merah kecil |

### Tipografi

| Level | HP | Laptop |
|---|---|---|
| Hero headline | `text-5xl` bold `tracking-tight` | `text-8xl` bold `tracking-tight` |
| Sub-header | `text-lg` medium, opacity 80% | `text-xl` medium, opacity 80% |
| Body | `text-sm` `leading-relaxed` | sama |
| Label | `text-[10px]` uppercase `tracking-widest` | sama |

Label 10px hanya untuk info pendukung. Info penting seperti total harga dan kode tiket selalu ditampilkan besar.

### Konten jujur

- **Ticker di bagian bawah halaman klien:** logo tech stack yang dikuasai tim (Java, MySQL, Tailwind, dll), warna putih atau abu-abu.
- **Trust badge:** hanya klaim yang memang benar dari produk ini, misalnya "Estimasi gratis" dan "Harga transparan". Tidak ada logo partner atau penghargaan palsu.

### Aksesibilitas

- Saat setting *reduce motion* aktif (`prefers-reduced-motion`), animasi slide-up dan float dimatikan.

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

### Alur kerja setiap halaman

1. Usulkan isi halaman: elemen yang tampil, urutannya, dan draft copywriting ID + EN.
2. User menyetujui atau mengoreksi.
3. Kerjakan halamannya.
4. Cek manual: `mvnw compile` berhasil, halaman terbuka di Jetty, tampilan di lebar 375px dan 1440px, serta tampilan dalam bahasa ID dan EN.
5. User review di browser.
6. Commit dengan pesan bahasa Inggris, contohnya `feat: add landing page prototype`.

**Berhenti setelah setiap halaman** dan tunggu review user sebelum lanjut ke halaman berikutnya.

### Cara menjalankan saat develop

```
npm run dev              # terminal 1: Tailwind watch
.\mvnw.cmd jetty:run     # terminal 2: server, buka http://localhost:8080/pbo-java/
```

## 9. Hal yang dicek setelah prototype jadi

- Design system secara keseluruhan (warna, posisi glass card, warna status). Disetujui sementara dan akan dinilai ulang dari tampilan asli.
- Nama aplikasi "Bisa." masih sementara.
- Switch bahasa me-reload halaman, jadi pilihan di kalkulator dan hasil cek status akan hilang. Cara menanganinya dibahas saat mengerjakan halaman Estimasi.
- Gambar portofolio di `seed.sql` memakai `placehold.co`, yang butuh internet. Gambar pengganti lokal dibahas saat mengerjakan halaman Landing.
