package com.pbojava.dao;

import com.pbojava.model.ProjectRequest;
import com.pbojava.model.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface ProjectRequestDAO {

    /** Semua pengajuan, yang terbaru di atas. */
    List<ProjectRequest> findAll();

    List<ProjectRequest> findByStatus(RequestStatus status);

    Optional<ProjectRequest> findById(int id);

    /** Untuk halaman cek status. Kode tiket dan email harus cocok dua-duanya. */
    Optional<ProjectRequest> findByTicketAndEmail(String kodeTiket, String email);
}
