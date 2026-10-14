package com.pbojava.dao;

import com.pbojava.dao.memory.InMemoryPortfolioDAO;

/**
 * Satu-satunya tempat yang menentukan sumber data.
 * Saat pindah ke database, cukup ganti implementasi di sini (misalnya new JdbcPortfolioDAO()).
 */
public final class DAOFactory {

    private static final PortfolioDAO PORTFOLIO_DAO = new InMemoryPortfolioDAO();

    private DAOFactory() {
    }

    public static PortfolioDAO portfolioDAO() {
        return PORTFOLIO_DAO;
    }
}
