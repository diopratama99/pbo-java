SET NAMES utf8mb4;

-- Untuk database yang dibuat sebelum backend selesai (schema.sql versi lama).
-- Menambah kolom bahasa Inggris dan tabel Tim. Setelah ini, jalankan db/seed.sql untuk mengisi datanya.
-- Database baru tidak perlu file ini, karena schema.sql sudah berisi semuanya.

ALTER TABLE master_features
    ADD COLUMN nama_fitur_en VARCHAR(100) AFTER nama_fitur,
    ADD COLUMN deskripsi_en VARCHAR(255) AFTER deskripsi;

ALTER TABLE portfolios
    ADD COLUMN judul_en VARCHAR(150) AFTER judul,
    ADD COLUMN deskripsi_en TEXT AFTER deskripsi;

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
    FOREIGN KEY (member_id) REFERENCES team_members(id) ON DELETE CASCADE
);
