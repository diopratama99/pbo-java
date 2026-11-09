package com.pbojava.dao;

import com.pbojava.model.feature.Feature;

import java.util.List;
import java.util.Optional;

public interface FeatureDAO {

    /** Semua fitur, termasuk yang nonaktif. Dipakai halaman admin. */
    List<Feature> findAll();

    /** Hanya fitur aktif. Dipakai kalkulator klien. */
    List<Feature> findActive();

    Optional<Feature> findById(int id);

    /** Tambah fitur baru kalau id-nya 0, selain itu ubah fitur yang ada. Mengembalikan id fitur. */
    int save(Feature feature);

    /**
     * Fitur tidak pernah dihapus, cukup dinonaktifkan,
     * supaya pengajuan lama yang memakai fitur ini tetap utuh.
     */
    void setAktif(int id, boolean aktif);
}
