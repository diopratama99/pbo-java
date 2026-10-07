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
