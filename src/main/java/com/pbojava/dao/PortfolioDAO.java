package com.pbojava.dao;

import com.pbojava.model.Portfolio;

import java.util.List;

public interface PortfolioDAO {

    List<Portfolio> findAll();
}
