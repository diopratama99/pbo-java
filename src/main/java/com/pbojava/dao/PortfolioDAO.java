package com.pbojava.dao;

import com.pbojava.model.Portfolio;

import java.util.List;
import java.util.Optional;

public interface PortfolioDAO {

    List<Portfolio> findAll();

    Optional<Portfolio> findById(int id);
}
