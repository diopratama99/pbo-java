SET NAMES utf8mb4;

-- Riwayat perubahan status pengajuan. Setelah ini, jalankan db/seed.sql
-- supaya pengajuan contoh punya riwayat dengan tanggal yang masuk akal.

CREATE TABLE request_status_history (
    id INT PRIMARY KEY AUTO_INCREMENT,
    request_id INT NOT NULL,
    status ENUM('PENDING', 'REVIEW', 'APPROVED', 'REJECTED') NOT NULL,
    admin_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (request_id) REFERENCES project_requests(id) ON DELETE CASCADE,
    FOREIGN KEY (admin_id) REFERENCES admins(id) ON DELETE SET NULL
);

-- Pengajuan yang sudah ada belum punya riwayat. Isi seadanya dari data yang tersedia:
-- tanggal pengajuan untuk PENDING, dan updated_at untuk status sekarang (adminnya tidak diketahui).
-- Kalau statusnya sudah APPROVED/REJECTED, tahap REVIEW di antaranya tidak tercatat.
INSERT INTO request_status_history (request_id, status, created_at)
SELECT id, 'PENDING', created_at FROM project_requests;

INSERT INTO request_status_history (request_id, status, created_at)
SELECT id, status, updated_at FROM project_requests WHERE status <> 'PENDING';
