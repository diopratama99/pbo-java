# Diagram database

Diagram ini mengikuti isi [schema.sql](schema.sql). Kalau schema berubah, ubah juga diagram ini.

Diagram bisa dilihat langsung di GitHub, atau di VS Code lewat Markdown Preview (`Ctrl+Shift+V`).

## Schema sekarang

```mermaid
erDiagram
    clients ||--o{ project_requests : "mengajukan"
    project_requests ||--o{ request_features : "berisi"
    master_features ||--o{ request_features : "dipilih di"
    team_members ||--o{ team_experiences : "punya"

    admins {
        int id PK
        varchar username UK
        varchar nama
        varchar password_hash "PBKDF2-SHA256 + salt"
        varchar salt
    }

    clients {
        int id PK
        varchar nama
        varchar instansi "boleh kosong"
        varchar email UK "klien dengan email sama memakai baris yang sama"
        varchar no_wa
        timestamp created_at
    }

    portfolios {
        int id PK
        varchar judul
        varchar judul_en
        text deskripsi
        text deskripsi_en
        varchar tech_stack
        varchar image_url
    }

    master_features {
        int id PK
        enum kategori "PLATFORM, DATABASE, CORE, INTEGRATION"
        varchar nama_fitur
        varchar nama_fitur_en
        varchar deskripsi
        varchar deskripsi_en
        decimal harga_dasar
        decimal pengali "hanya PLATFORM"
        decimal biaya_setup "hanya INTEGRATION"
        boolean is_active "FALSE = disembunyikan dari kalkulator"
    }

    project_requests {
        int id PK
        varchar kode_tiket UK "contoh DA-7K2P9Q"
        int client_id FK
        text custom_notes
        decimal subtotal
        decimal diskon
        decimal total_estimasi
        enum status "PENDING, REVIEW, APPROVED, REJECTED"
        varchar wag_link "diisi saat APPROVED"
        varchar alasan_ditolak "diisi saat REJECTED"
        timestamp created_at
        timestamp updated_at
    }

    request_features {
        int request_id PK, FK
        int feature_id PK, FK
        decimal harga_saat_itu "harga saat pengajuan dibuat"
    }

    team_members {
        int id PK
        varchar nama
        varchar foto
        varchar posisi
        varchar posisi_en
        varchar pendidikan
        varchar pendidikan_en
        int urutan "urutan tampil di halaman Tim"
    }

    team_experiences {
        int id PK
        int member_id FK
        varchar deskripsi
        varchar deskripsi_en
    }
```

- `admins` tidak berelasi dengan tabel lain. Tabel ini hanya dipakai untuk login admin.
- `portfolios` juga berdiri sendiri. Isinya ditampilkan di beranda.
- `team_members` dan `team_experiences` dipakai halaman Tim. Pengalaman ikut terhapus kalau anggotanya dihapus (`ON DELETE CASCADE`).
- Kolom berakhiran `_en` berisi teks bahasa Inggris. Kalau kosong, aplikasi memakai versi Indonesia.
- `request_features` adalah tabel penghubung many-to-many antara pengajuan dan fitur. Primary key-nya gabungan `request_id` dan `feature_id`, jadi satu fitur hanya bisa dipilih sekali per pengajuan.
