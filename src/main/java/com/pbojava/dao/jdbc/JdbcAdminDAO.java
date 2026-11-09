package com.pbojava.dao.jdbc;

import com.pbojava.dao.AdminDAO;
import com.pbojava.dao.DataAccessException;
import com.pbojava.model.Admin;
import com.pbojava.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/** Akun admin dari tabel admins. Akun baru dibuat lewat util.BuatAdmin. */
public class JdbcAdminDAO implements AdminDAO {

    @Override
    public Optional<Admin> findByUsername(String username) {
        String sql = "SELECT id, username, nama, password_hash, salt FROM admins WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Admin(rs.getInt("id"), rs.getString("username"), rs.getString("nama"),
                        rs.getString("password_hash"), rs.getString("salt")));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal membaca akun admin", e);
        }
    }
}
