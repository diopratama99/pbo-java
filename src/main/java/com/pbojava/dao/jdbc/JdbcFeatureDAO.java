package com.pbojava.dao.jdbc;

import com.pbojava.dao.DataAccessException;
import com.pbojava.dao.FeatureDAO;
import com.pbojava.model.LocalizedText;
import com.pbojava.model.feature.CoreFeature;
import com.pbojava.model.feature.DatabaseFeature;
import com.pbojava.model.feature.Feature;
import com.pbojava.model.feature.IntegrationFeature;
import com.pbojava.model.feature.PlatformFeature;
import com.pbojava.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Fitur dan harga dari tabel master_features. */
public class JdbcFeatureDAO implements FeatureDAO {

    static final String KOLOM = "mf.id, mf.kategori, mf.nama_fitur, mf.nama_fitur_en, mf.deskripsi, mf.deskripsi_en, "
            + "mf.harga_dasar, mf.pengali, mf.biaya_setup, mf.is_active";

    /** Urutan tampil: per kategori sesuai langkah di kalkulator, lalu urutan id. */
    static final String URUTAN = "FIELD(mf.kategori, 'PLATFORM', 'DATABASE', 'CORE', 'INTEGRATION'), mf.id";

    @Override
    public List<Feature> findAll() {
        return query("SELECT " + KOLOM + " FROM master_features mf ORDER BY " + URUTAN);
    }

    @Override
    public List<Feature> findActive() {
        return query("SELECT " + KOLOM + " FROM master_features mf WHERE mf.is_active = TRUE ORDER BY " + URUTAN);
    }

    @Override
    public Optional<Feature> findById(int id) {
        String sql = "SELECT " + KOLOM + " FROM master_features mf WHERE mf.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal membaca fitur " + id, e);
        }
    }

    @Override
    public int save(Feature feature) {
        boolean baru = feature.getId() == 0;
        String sql = baru
                ? "INSERT INTO master_features (kategori, nama_fitur, nama_fitur_en, deskripsi, deskripsi_en, "
                        + "harga_dasar, pengali, biaya_setup, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
                : "UPDATE master_features SET kategori = ?, nama_fitur = ?, nama_fitur_en = ?, deskripsi = ?, "
                        + "deskripsi_en = ?, harga_dasar = ?, pengali = ?, biaya_setup = ?, is_active = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, feature.getKategori());
            stmt.setString(2, feature.getNama().getId());
            stmt.setString(3, feature.getNama().getEn());
            stmt.setString(4, feature.getDeskripsi().getId());
            stmt.setString(5, feature.getDeskripsi().getEn());
            stmt.setBigDecimal(6, feature.getHargaDasar());
            setNullable(stmt, 7, feature instanceof PlatformFeature platform ? platform.getPengali() : null);
            setNullable(stmt, 8, feature instanceof IntegrationFeature integrasi ? integrasi.getBiayaSetup() : null);
            stmt.setBoolean(9, feature.isAktif());
            if (!baru) {
                stmt.setInt(10, feature.getId());
            }
            stmt.executeUpdate();

            if (!baru) {
                return feature.getId();
            }
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal menyimpan fitur", e);
        }
    }

    @Override
    public void setAktif(int id, boolean aktif) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE master_features SET is_active = ? WHERE id = ?")) {
            stmt.setBoolean(1, aktif);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mengubah status fitur " + id, e);
        }
    }

    /**
     * Membuat object subclass yang sesuai dengan kolom kategori.
     * Dipakai juga oleh JdbcProjectRequestDAO saat membaca fitur sebuah pengajuan.
     */
    static Feature map(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        LocalizedText nama = JdbcUtil.text(rs.getString("nama_fitur"), rs.getString("nama_fitur_en"));
        LocalizedText deskripsi = JdbcUtil.text(rs.getString("deskripsi"), rs.getString("deskripsi_en"));
        BigDecimal harga = rs.getBigDecimal("harga_dasar");
        boolean aktif = rs.getBoolean("is_active");

        return switch (rs.getString("kategori")) {
            case "PLATFORM" -> new PlatformFeature(id, nama, deskripsi, harga,
                    orDefault(rs.getBigDecimal("pengali"), BigDecimal.ONE), aktif);
            case "DATABASE" -> new DatabaseFeature(id, nama, deskripsi, harga, aktif);
            case "CORE" -> new CoreFeature(id, nama, deskripsi, harga, aktif);
            case "INTEGRATION" -> new IntegrationFeature(id, nama, deskripsi, harga,
                    orDefault(rs.getBigDecimal("biaya_setup"), BigDecimal.ZERO), aktif);
            default -> throw new SQLException("Kategori tidak dikenal: " + rs.getString("kategori"));
        };
    }

    private List<Feature> query(String sql) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<Feature> hasil = new ArrayList<>();
            while (rs.next()) {
                hasil.add(map(rs));
            }
            return hasil;
        } catch (SQLException e) {
            throw new DataAccessException("Gagal membaca daftar fitur", e);
        }
    }

    private static void setNullable(PreparedStatement stmt, int index, BigDecimal value) throws SQLException {
        if (value == null) {
            stmt.setNull(index, Types.DECIMAL);
        } else {
            stmt.setBigDecimal(index, value);
        }
    }

    private static BigDecimal orDefault(BigDecimal value, BigDecimal fallback) {
        return value == null ? fallback : value;
    }
}
