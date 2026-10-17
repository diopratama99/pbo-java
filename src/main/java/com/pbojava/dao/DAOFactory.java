package com.pbojava.dao;

import com.pbojava.dao.memory.InMemoryAdminDAO;
import com.pbojava.dao.memory.InMemoryFeatureDAO;
import com.pbojava.dao.memory.InMemoryPortfolioDAO;
import com.pbojava.dao.memory.InMemoryProjectRequestDAO;
import com.pbojava.dao.memory.InMemoryTeamDAO;

/**
 * Satu-satunya tempat yang menentukan sumber data.
 * Saat pindah ke database, cukup ganti implementasi di sini (misalnya new JdbcPortfolioDAO()).
 */
public final class DAOFactory {

    private static final PortfolioDAO PORTFOLIO_DAO = new InMemoryPortfolioDAO();
    private static final FeatureDAO FEATURE_DAO = new InMemoryFeatureDAO();
    private static final ProjectRequestDAO PROJECT_REQUEST_DAO = new InMemoryProjectRequestDAO(FEATURE_DAO);
    private static final TeamDAO TEAM_DAO = new InMemoryTeamDAO();
    private static final AdminDAO ADMIN_DAO = new InMemoryAdminDAO();

    private DAOFactory() {
    }

    public static PortfolioDAO portfolioDAO() {
        return PORTFOLIO_DAO;
    }

    public static FeatureDAO featureDAO() {
        return FEATURE_DAO;
    }

    public static ProjectRequestDAO projectRequestDAO() {
        return PROJECT_REQUEST_DAO;
    }

    public static TeamDAO teamDAO() {
        return TEAM_DAO;
    }

    public static AdminDAO adminDAO() {
        return ADMIN_DAO;
    }
}
