SET NAMES utf8mb4;

-- Data contoh, silakan ubah nama dan harganya.
-- Akun admin belum dibuat di sini karena format hash-nya harus sama dengan PasswordUtil di Java.

INSERT INTO master_features (kategori, nama_fitur, deskripsi, harga_dasar, pengali, biaya_setup) VALUES
('PLATFORM',    'Website',               'Aplikasi diakses lewat browser',                3000000, 1.00, NULL),
('PLATFORM',    'Aplikasi Mobile',       'Aplikasi Android/iOS',                          5000000, 1.30, NULL),
('PLATFORM',    'Website + Mobile',      'Versi web dan aplikasi mobile',                 7000000, 1.60, NULL),

('DATABASE',    'MySQL',                 'Database relasional yang umum dipakai',         1000000, NULL, NULL),
('DATABASE',    'PostgreSQL',            'Database relasional untuk data yang kompleks',  1000000, NULL, NULL),

('CORE',        'Login & Registrasi',    'Pengguna bisa daftar dan masuk ke akun',        1500000, NULL, NULL),
('CORE',        'Dashboard Admin',       'Halaman pengelolaan data untuk pemilik',        2500000, NULL, NULL),
('CORE',        'Chat',                  'Pengguna bisa saling berkirim pesan',           3000000, NULL, NULL),
('CORE',        'Notifikasi',            'Pemberitahuan ke pengguna',                     1000000, NULL, NULL),
('CORE',        'Laporan & Export PDF',  'Rekap data yang bisa diunduh',                  1500000, NULL, NULL),
('CORE',        'Pencarian & Filter',    'Cari dan saring data dengan cepat',             1000000, NULL, NULL),

('INTEGRATION', 'Payment Gateway',       'Pembayaran online (transfer, e-wallet)',        2000000, NULL, 500000),
('INTEGRATION', 'Google Maps',           'Menampilkan peta dan lokasi',                   1000000, NULL, 250000),
('INTEGRATION', 'Notifikasi WhatsApp',   'Kirim pesan otomatis ke WhatsApp',              1000000, NULL, 300000),
('INTEGRATION', 'Login dengan Google',   'Masuk pakai akun Google',                        750000, NULL, 250000);

INSERT INTO portfolios (judul, deskripsi, tech_stack, image_url) VALUES
('Contoh Proyek 1', 'Ganti dengan proyek yang pernah dikerjakan.', 'Java, MySQL', 'https://placehold.co/600x400'),
('Contoh Proyek 2', 'Ganti dengan proyek yang pernah dikerjakan.', 'PHP, Bootstrap', 'https://placehold.co/600x400');
