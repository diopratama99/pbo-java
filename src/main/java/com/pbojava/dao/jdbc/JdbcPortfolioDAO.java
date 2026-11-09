package com.pbojava.dao.jdbc;

import com.pbojava.dao.DataAccessException;
import com.pbojava.dao.PortfolioDAO;
import com.pbojava.model.Portfolio;
import com.pbojava.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Portofolio dari tabel portfolios. */
public class JdbcPortfolioDAO implements PortfolioDAO {

    private static final String KOLOM = "id, judul, judul_en, deskripsi, deskripsi_en, tech_stack, image_url";

    @Override
    public List<Portfolio> findAll() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT " + KOLOM + " FROM portfolios ORDER BY id");
             ResultSet rs = stmt.executeQuery()) {
            List<Portfolio> hasil = new ArrayList<>();
            while (rs.next()) {
                hasil.add(map(rs));
            }
            return hasil;
        } catch (SQLException e) {
            throw new DataAccessException("Gagal membaca daftar portofolio", e);
        }
    }

    @Override
    public Optional<Portfolio> findById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT " + KOLOM + " FROM portfolios WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal membaca portofolio " + id, e);
        }
    }

    @Override
    public int save(Portfolio portfolio) {
        boolean baru = portfolio.getId() == 0;
        String sql = baru
                ? "INSERT INTO portfolios (judul, judul_en, deskripsi, deskripsi_en, tech_stack, image_url) "
                        + "VALUES (?, ?, ?, ?, ?, ?)"
                : "UPDATE portfolios SET judul = ?, judul_en = ?, deskripsi = ?, deskripsi_en = ?, "
                        + "tech_stack = ?, image_url = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, portfolio.getJudul().getId());
            stmt.setString(2, portfolio.getJudul().getEn());
            stmt.setString(3, portfolio.getDeskripsi().getId());
            stmt.setString(4, portfolio.getDeskripsi().getEn());
            stmt.setString(5, portfolio.getTechStack());
            stmt.setString(6, portfolio.getImageUrl());
            if (!baru) {
                stmt.setInt(7, portfolio.getId());
            }
            stmt.executeUpdate();

            if (!baru) {
                return portfolio.getId();
            }
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal menyimpan portofolio", e);
        }
    }

    @Override
    public void delete(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM portfolios WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Gagal menghapus portofolio " + id, e);
        }
    }

    private static Portfolio map(ResultSet rs) throws SQLException {
        String techStack = rs.getString("tech_stack");
        String imageUrl = rs.getString("image_url");
        return new Portfolio(rs.getInt("id"),
                JdbcUtil.text(rs.getString("judul"), rs.getString("judul_en")),
                JdbcUtil.text(rs.getString("deskripsi"), rs.getString("deskripsi_en")),
                techStack == null ? "" : techStack,
                imageUrl == null ? "" : imageUrl);
    }
}
