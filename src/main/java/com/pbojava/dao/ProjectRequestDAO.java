package com.pbojava.dao;

import com.pbojava.model.ProjectRequest;
import com.pbojava.model.RequestStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProjectRequestDAO {

    /** Semua pengajuan, yang terbaru di atas. Daftar fiturnya tidak ikut diambil. */
    List<ProjectRequest> findAll();

    /** Daftar fiturnya tidak ikut diambil. */
    List<ProjectRequest> findByStatus(RequestStatus status);

    /** Jumlah pengajuan untuk setiap status, untuk tombol filter di halaman admin. */
    Map<RequestStatus, Integer> countByStatus();

    Optional<ProjectRequest> findById(int id);

    /** Untuk halaman cek status. Kode tiket dan email harus cocok dua-duanya. */
    Optional<ProjectRequest> findByTicketAndEmail(String kodeTiket, String email);

    boolean ticketExists(String kodeTiket);

    /**
     * Simpan pengajuan baru beserta kliennya dan fitur yang dipilih dalam satu transaksi.
     * Klien dengan email yang sama memakai data klien yang sama. Mengembalikan id pengajuan.
     */
    int insert(ProjectRequest request);

    /**
     * Simpan status, link grup, dan alasan penolakan.
     * Hanya berhasil kalau status di database masih {@code statusLama}, supaya dua admin
     * yang menekan tombol bersamaan tidak saling menimpa. Mengembalikan false kalau gagal.
     */
    boolean updateStatus(ProjectRequest request, RequestStatus statusLama);
}
