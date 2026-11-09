package com.pbojava.dao;

import com.pbojava.dao.jdbc.JdbcAdminDAO;
import com.pbojava.dao.jdbc.JdbcFeatureDAO;
import com.pbojava.dao.jdbc.JdbcPortfolioDAO;
import com.pbojava.dao.jdbc.JdbcProjectRequestDAO;
import com.pbojava.dao.jdbc.JdbcTeamDAO;

/**
 * Satu-satunya tempat yang menentukan sumber data.
 * Servlet hanya mengenal interface DAO, jadi mengganti sumber data cukup di sini.
 */
public final class DAOFactory {

    private static final PortfolioDAO PORTFOLIO_DAO = new JdbcPortfolioDAO();
    private static final FeatureDAO FEATURE_DAO = new JdbcFeatureDAO();
    private static final ProjectRequestDAO PROJECT_REQUEST_DAO = new JdbcProjectRequestDAO();
    private static final TeamDAO TEAM_DAO = new JdbcTeamDAO();
    private static final AdminDAO ADMIN_DAO = new JdbcAdminDAO();

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
