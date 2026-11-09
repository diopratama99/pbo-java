package com.pbojava.dao;

import com.pbojava.model.Portfolio;

import java.util.List;
import java.util.Optional;

public interface PortfolioDAO {

    List<Portfolio> findAll();

    Optional<Portfolio> findById(int id);

    /** Tambah portofolio baru kalau id-nya 0, selain itu ubah yang ada. Mengembalikan id portofolio. */
    int save(Portfolio portfolio);

    void delete(int id);
}
