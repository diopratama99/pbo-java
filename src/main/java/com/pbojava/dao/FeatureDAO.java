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
}
