SET NAMES utf8mb4;

-- Data contoh. Semua baris memakai id tetap dan "ON DUPLICATE KEY UPDATE",
-- jadi file ini aman dijalankan ulang tanpa membuat data dobel.
-- Hati-hati: menjalankan ulang akan mengembalikan harga fitur ke angka di file ini.
--
-- Akun admin tidak dibuat di sini karena password-nya harus di-hash oleh PasswordUtil.
-- Buat lewat: mvnw compile exec:java -Dexec.mainClass=com.pbojava.util.BuatAdmin -Dexec.args="admin 'Admin Bisa.'"

INSERT INTO master_features (id, kategori, nama_fitur, nama_fitur_en, deskripsi, deskripsi_en, harga_dasar, pengali, biaya_setup, is_active) VALUES
(1,  'PLATFORM',    'Website',              'Website',                'Aplikasi diakses lewat browser',               'Accessed through a web browser',             3000000, 1.00, NULL,   TRUE),
(2,  'PLATFORM',    'Aplikasi Mobile',      'Mobile App',             'Aplikasi Android/iOS',                         'An Android/iOS app',                         5000000, 1.30, NULL,   TRUE),
(3,  'PLATFORM',    'Website + Mobile',     'Website + Mobile',       'Versi web dan aplikasi mobile',                'Both a web version and a mobile app',        7000000, 1.60, NULL,   TRUE),

(4,  'DATABASE',    'MySQL',                'MySQL',                  'Database relasional yang umum dipakai',        'A widely used relational database',          1000000, NULL, NULL,   TRUE),
(5,  'DATABASE',    'PostgreSQL',           'PostgreSQL',             'Database relasional untuk data yang kompleks', 'A relational database for complex data',     1000000, NULL, NULL,   TRUE),

(6,  'CORE',        'Login & Registrasi',   'Login & Sign Up',        'Pengguna bisa daftar dan masuk ke akun',       'Users can sign up and log in',               1500000, NULL, NULL,   TRUE),
(7,  'CORE',        'Dashboard Admin',      'Admin Dashboard',        'Halaman pengelolaan data untuk pemilik',       'A page for owners to manage data',           2500000, NULL, NULL,   TRUE),
(8,  'CORE',        'Chat',                 'Chat',                   'Pengguna bisa saling berkirim pesan',          'Users can message each other',               3000000, NULL, NULL,   TRUE),
(9,  'CORE',        'Notifikasi',           'Notifications',          'Pemberitahuan ke pengguna',                    'Alerts sent to users',                       1000000, NULL, NULL,   TRUE),
(10, 'CORE',        'Laporan & Export PDF', 'Reports & PDF Export',   'Rekap data yang bisa diunduh',                 'Downloadable data summaries',                1500000, NULL, NULL,   TRUE),
(11, 'CORE',        'Pencarian & Filter',   'Search & Filter',        'Cari dan saring data dengan cepat',            'Find and filter data quickly',               1000000, NULL, NULL,   TRUE),

(12, 'INTEGRATION', 'Payment Gateway',      'Payment Gateway',        'Pembayaran online (transfer, e-wallet)',       'Online payments (bank transfer, e-wallet)',  2000000, NULL, 500000, TRUE),
(13, 'INTEGRATION', 'Google Maps',          'Google Maps',            'Menampilkan peta dan lokasi',                  'Show maps and locations',                    1000000, NULL, 250000, TRUE),
(14, 'INTEGRATION', 'Notifikasi WhatsApp',  'WhatsApp Notifications', 'Kirim pesan otomatis ke WhatsApp',             'Send automatic WhatsApp messages',           1000000, NULL, 300000, TRUE),
(15, 'INTEGRATION', 'Login dengan Google',  'Sign in with Google',    'Masuk pakai akun Google',                      'Log in with a Google account',                750000, NULL, 250000, TRUE),

-- Contoh fitur nonaktif: tidak muncul di kalkulator, tapi masih ada di halaman admin
(16, 'CORE',        'Live Streaming',       'Live Streaming',         'Siaran video langsung',                        'Live video broadcasts',                      6000000, NULL, NULL,   FALSE)
AS baru ON DUPLICATE KEY UPDATE
    kategori = baru.kategori, nama_fitur = baru.nama_fitur, nama_fitur_en = baru.nama_fitur_en,
    deskripsi = baru.deskripsi, deskripsi_en = baru.deskripsi_en, harga_dasar = baru.harga_dasar,
    pengali = baru.pengali, biaya_setup = baru.biaya_setup, is_active = baru.is_active;

INSERT INTO portfolios (id, judul, judul_en, deskripsi, deskripsi_en, tech_stack, image_url) VALUES
(1, 'Sistem Kasir UMKM', 'Point of Sale for Small Shops',
    'Aplikasi kasir berbasis web untuk mencatat penjualan dan stok barang harian.',
    'A web-based cashier app to record daily sales and stock.',
    'Java, MySQL, Tailwind CSS', '/assets/img/portfolio/kasir.svg'),
(2, 'Presensi Kampus', 'Campus Attendance',
    'Presensi mahasiswa dengan QR code, lengkap dengan rekap per mata kuliah.',
    'Student attendance with QR codes, plus a recap for every course.',
    'Kotlin, Firebase', '/assets/img/portfolio/presensi.svg'),
(3, 'Company Profile Kopi Lokal', 'Local Coffee Company Profile',
    'Website profil usaha dengan menu, lokasi toko, dan pemesanan lewat WhatsApp.',
    'A business website with the menu, store locations, and ordering via WhatsApp.',
    'PHP, Bootstrap', '/assets/img/portfolio/kopi.svg')
AS baru ON DUPLICATE KEY UPDATE
    judul = baru.judul, judul_en = baru.judul_en, deskripsi = baru.deskripsi, deskripsi_en = baru.deskripsi_en,
    tech_stack = baru.tech_stack, image_url = baru.image_url;

-- Foto: simpan PNG tanpa background di src/main/webapp/assets/img/team/ dengan nama file di bawah.
-- Selama file PNG-nya belum ada, halaman Tim menampilkan siluet placeholder.
INSERT INTO team_members (id, nama, foto, posisi, posisi_en, pendidikan, pendidikan_en, urutan) VALUES
(1, 'Dio',       '/assets/img/team/dio.png',       'Back End Developer', 'Back End Developer', 'S1 Teknik Informatika', 'Bachelor of Informatics Engineering', 1),
(2, 'Agmar',     '/assets/img/team/agmar.png',     'Software Engineer',  'Software Engineer',  'S1 Teknik Informatika', 'Bachelor of Informatics Engineering', 2),
(3, 'Wulandari', '/assets/img/team/wulandari.png', 'UI/UX Designer',     'UI/UX Designer',     'S1 Teknik Informatika', 'Bachelor of Informatics Engineering', 3)
AS baru ON DUPLICATE KEY UPDATE
    nama = baru.nama, foto = baru.foto, posisi = baru.posisi, posisi_en = baru.posisi_en,
    pendidikan = baru.pendidikan, pendidikan_en = baru.pendidikan_en, urutan = baru.urutan;

INSERT INTO team_experiences (id, member_id, deskripsi, deskripsi_en) VALUES
(1, 1, 'Membangun REST API untuk aplikasi kasir (contoh)', 'Built a REST API for a cashier app (example)'),
(2, 1, 'Mendesain database untuk sistem presensi (contoh)', 'Designed the database for an attendance system (example)'),
(3, 2, '5 tahun pengalaman sebagai Front End Developer dan Fullstack Developer di perusahaan fintech',
       '5 years of experience as a Front End Developer and Fullstack Developer at a fintech company'),
(4, 3, 'Mendesain aplikasi mobile untuk UMKM (contoh)', 'Designed a mobile app for small businesses (example)'),
(5, 3, 'Melakukan riset pengguna untuk website kampus (contoh)', 'Ran user research for a campus website (example)')
AS baru ON DUPLICATE KEY UPDATE
    member_id = baru.member_id, deskripsi = baru.deskripsi, deskripsi_en = baru.deskripsi_en;

-- Pengajuan contoh, satu untuk tiap status, supaya halaman cek status dan admin bisa langsung dicoba.
-- Tiket untuk demo cek status (email: klien@contoh.com):
-- DA-PEND01 (PENDING), DA-REVW01 (REVIEW), DA-APPR01 (APPROVED), DA-REJT01 (REJECTED).
-- Harganya mengikuti aturan di plan.md bagian 6, sama dengan hasil QuotationCalculator.
INSERT INTO clients (id, nama, instansi, email, no_wa) VALUES
(1, 'Rina Wijaya',  'Toko Rina',    'klien@contoh.com', '081234567890'),
(2, 'Budi Santoso', 'CV Maju Jaya', 'budi@contoh.com',  '081298765432'),
(3, 'Sari Lestari', NULL,           'sari@contoh.com',  '085711223344')
AS baru ON DUPLICATE KEY UPDATE
    nama = baru.nama, instansi = baru.instansi, email = baru.email, no_wa = baru.no_wa;

INSERT INTO project_requests (id, kode_tiket, client_id, custom_notes, subtotal, diskon, total_estimasi, status, wag_link, alasan_ditolak, created_at) VALUES
-- Website, MySQL, Login (x1.0), Dashboard Admin (x1.0)
(1, 'DA-PEND01', 1, 'Butuh fitur cetak struk untuk kasir.', 8000000, 0, 8000000, 'PENDING', NULL, NULL, '2026-10-14 09:30:00'),
-- Contoh hitungan di plan.md: Mobile, MySQL, Login (x1.3), Chat (x1.3), Payment Gateway (+ setup)
(2, 'DA-REVW01', 1, NULL, 14350000, 0, 14350000, 'REVIEW', NULL, NULL, '2026-10-12 14:05:00'),
-- Website + Mobile (x1.6) dengan 5 fitur, jadi dapat diskon bundling 10%
(3, 'DA-APPR01', 1, 'Aplikasi untuk 3 cabang toko.', 19650000, 1965000, 17685000, 'APPROVED',
    'https://chat.whatsapp.com/ContohLinkGrup', NULL, '2026-10-08 10:00:00'),
(4, 'DA-REJT01', 1, 'Mau seperti aplikasi besar.', 7000000, 0, 7000000, 'REJECTED', NULL,
    'Kebutuhannya belum jelas. Silakan ajukan ulang dengan daftar fitur yang lebih detail.', '2026-10-05 16:45:00'),
(5, 'DA-8F3K2M', 2, NULL, 9250000, 0, 9250000, 'PENDING', NULL, NULL, '2026-10-15 08:10:00'),
(6, 'DA-Q7N4TX', 3, 'Website katalog produk handmade.', 6000000, 0, 6000000, 'REVIEW', NULL, NULL, '2026-10-13 19:20:00')
AS baru ON DUPLICATE KEY UPDATE
    kode_tiket = baru.kode_tiket, client_id = baru.client_id, custom_notes = baru.custom_notes,
    subtotal = baru.subtotal, diskon = baru.diskon, total_estimasi = baru.total_estimasi, status = baru.status,
    wag_link = baru.wag_link, alasan_ditolak = baru.alasan_ditolak, created_at = baru.created_at;

INSERT INTO request_features (request_id, feature_id, harga_saat_itu) VALUES
(1, 1, 3000000), (1, 4, 1000000), (1, 6, 1500000), (1, 7, 2500000),
(2, 2, 5000000), (2, 4, 1000000), (2, 6, 1950000), (2, 8, 3900000), (2, 12, 2500000),
(3, 3, 7000000), (3, 5, 1000000), (3, 6, 2400000), (3, 7, 4000000), (3, 9, 1600000), (3, 10, 2400000), (3, 13, 1250000),
(4, 1, 3000000), (4, 4, 1000000), (4, 8, 3000000),
(5, 2, 5000000), (5, 4, 1000000), (5, 6, 1950000), (5, 9, 1300000),
(6, 1, 3000000), (6, 5, 1000000), (6, 11, 1000000), (6, 15, 1000000)
AS baru ON DUPLICATE KEY UPDATE harga_saat_itu = baru.harga_saat_itu;
