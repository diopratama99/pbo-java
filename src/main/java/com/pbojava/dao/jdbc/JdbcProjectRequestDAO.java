package com.pbojava.dao.jdbc;

import com.pbojava.dao.DataAccessException;
import com.pbojava.dao.ProjectRequestDAO;
import com.pbojava.model.Client;
import com.pbojava.model.ProjectRequest;
import com.pbojava.model.RequestFeature;
import com.pbojava.model.RequestStatus;
import com.pbojava.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Pengajuan dari tabel project_requests, beserta klien (clients) dan fiturnya (request_features). */
public class JdbcProjectRequestDAO implements ProjectRequestDAO {

    private static final String SELECT = "SELECT pr.id, pr.kode_tiket, pr.custom_notes, pr.subtotal, pr.diskon, "
            + "pr.total_estimasi, pr.status, pr.wag_link, pr.alasan_ditolak, pr.created_at, "
            + "c.id AS client_id, c.nama, c.instansi, c.email, c.no_wa "
            + "FROM project_requests pr JOIN clients c ON c.id = pr.client_id ";

    private static final String TERBARU = " ORDER BY pr.created_at DESC, pr.id DESC";

    @Override
    public List<ProjectRequest> findAll() {
        return queryList(SELECT + TERBARU, null);
    }

    @Override
    public List<ProjectRequest> findByStatus(RequestStatus status) {
        return queryList(SELECT + "WHERE pr.status = ?" + TERBARU, status.name());
    }

    @Override
    public Map<RequestStatus, Integer> countByStatus() {
        Map<RequestStatus, Integer> hasil = new EnumMap<>(RequestStatus.class);
        for (RequestStatus status : RequestStatus.values()) {
            hasil.put(status, 0);
        }
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT status, COUNT(*) AS jumlah FROM project_requests GROUP BY status");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                hasil.put(RequestStatus.valueOf(rs.getString("status")), rs.getInt("jumlah"));
            }
            return hasil;
        } catch (SQLException e) {
            throw new DataAccessException("Gagal menghitung pengajuan", e);
        }
    }

    @Override
    public Optional<ProjectRequest> findById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT + "WHERE pr.id = ?")) {
            stmt.setInt(1, id);
            return findOneWithFeatures(conn, stmt);
        } catch (SQLException e) {
            throw new DataAccessException("Gagal membaca pengajuan " + id, e);
        }
    }

    @Override
    public Optional<ProjectRequest> findByTicketAndEmail(String kodeTiket, String email) {
        // Kolom memakai collation utf8mb4_unicode_ci, jadi perbandingannya tidak membedakan huruf besar/kecil
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT + "WHERE pr.kode_tiket = ? AND c.email = ?")) {
            stmt.setString(1, kodeTiket.trim());
            stmt.setString(2, email.trim());
            return findOneWithFeatures(conn, stmt);
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mencari pengajuan", e);
        }
    }

    @Override
    public boolean ticketExists(String kodeTiket) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM project_requests WHERE kode_tiket = ?")) {
            stmt.setString(1, kodeTiket);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mengecek kode tiket", e);
        }
    }

    @Override
    public int insert(ProjectRequest request) {
        String sqlClient = "INSERT INTO clients (nama, instansi, email, no_wa) VALUES (?, ?, ?, ?) AS baru "
                + "ON DUPLICATE KEY UPDATE nama = baru.nama, instansi = baru.instansi, no_wa = baru.no_wa";
        String sqlRequest = "INSERT INTO project_requests (kode_tiket, client_id, custom_notes, subtotal, diskon, "
                + "total_estimasi, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlFitur = "INSERT INTO request_features (request_id, feature_id, harga_saat_itu) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            // Satu transaksi: kalau salah satu langkah gagal, semuanya dibatalkan
            conn.setAutoCommit(false);
            try {
                Client client = request.getClient();
                try (PreparedStatement stmt = conn.prepareStatement(sqlClient)) {
                    stmt.setString(1, client.getNama());
                    stmt.setString(2, client.getInstansi());
                    stmt.setString(3, client.getEmail());
                    stmt.setString(4, client.getNoWa());
                    stmt.executeUpdate();
                }
                int clientId = clientIdByEmail(conn, client.getEmail());

                int requestId;
                try (PreparedStatement stmt = conn.prepareStatement(sqlRequest, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, request.getKodeTiket());
                    stmt.setInt(2, clientId);
                    stmt.setString(3, request.getCustomNotes());
                    stmt.setBigDecimal(4, request.getSubtotal());
                    stmt.setBigDecimal(5, request.getDiskon());
                    stmt.setBigDecimal(6, request.getTotalEstimasi());
                    stmt.setString(7, request.getStatus().name());
                    stmt.executeUpdate();
                    try (ResultSet keys = stmt.getGeneratedKeys()) {
                        keys.next();
                        requestId = keys.getInt(1);
                    }
                }

                try (PreparedStatement stmt = conn.prepareStatement(sqlFitur)) {
                    for (RequestFeature item : request.getFitur()) {
                        stmt.setInt(1, requestId);
                        stmt.setInt(2, item.getFeature().getId());
                        stmt.setBigDecimal(3, item.getHargaSaatItu());
                        stmt.addBatch();
                    }
                    stmt.executeBatch();
                }

                conn.commit();
                return requestId;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal menyimpan pengajuan", e);
        }
    }

    @Override
    public boolean updateStatus(ProjectRequest request, RequestStatus statusLama) {
        String sql = "UPDATE project_requests SET status = ?, wag_link = ?, alasan_ditolak = ? "
                + "WHERE id = ? AND status = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, request.getStatus().name());
            stmt.setString(2, request.getWagLink());
            stmt.setString(3, request.getAlasanDitolak());
            stmt.setInt(4, request.getId());
            stmt.setString(5, statusLama.name());
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mengubah status pengajuan " + request.getId(), e);
        }
    }

    private List<ProjectRequest> queryList(String sql, String parameter) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (parameter != null) {
                stmt.setString(1, parameter);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                List<ProjectRequest> hasil = new ArrayList<>();
                while (rs.next()) {
                    hasil.add(map(rs, List.of()));
                }
                return hasil;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal membaca daftar pengajuan", e);
        }
    }

    private Optional<ProjectRequest> findOneWithFeatures(Connection conn, PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery()) {
            if (!rs.next()) {
                return Optional.empty();
            }
            return Optional.of(map(rs, findFeatures(conn, rs.getInt("id"))));
        }
    }

    private List<RequestFeature> findFeatures(Connection conn, int requestId) throws SQLException {
        String sql = "SELECT rf.harga_saat_itu, " + JdbcFeatureDAO.KOLOM + " FROM request_features rf "
                + "JOIN master_features mf ON mf.id = rf.feature_id WHERE rf.request_id = ? "
                + "ORDER BY " + JdbcFeatureDAO.URUTAN;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            try (ResultSet rs = stmt.executeQuery()) {
                List<RequestFeature> hasil = new ArrayList<>();
                while (rs.next()) {
                    hasil.add(new RequestFeature(JdbcFeatureDAO.map(rs), rs.getBigDecimal("harga_saat_itu")));
                }
                return hasil;
            }
        }
    }

    private static int clientIdByEmail(Connection conn, String email) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT id FROM clients WHERE email = ?")) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Klien dengan email " + email + " tidak ditemukan setelah disimpan");
                }
                return rs.getInt(1);
            }
        }
    }

    private static ProjectRequest map(ResultSet rs, List<RequestFeature> fitur) throws SQLException {
        Client client = new Client(rs.getInt("client_id"), rs.getString("nama"), rs.getString("instansi"),
                rs.getString("email"), rs.getString("no_wa"));
        return new ProjectRequest(rs.getInt("id"), rs.getString("kode_tiket"), client, fitur,
                rs.getString("custom_notes"), rs.getBigDecimal("subtotal"), rs.getBigDecimal("diskon"),
                rs.getBigDecimal("total_estimasi"), RequestStatus.valueOf(rs.getString("status")),
                rs.getString("wag_link"), rs.getString("alasan_ditolak"),
                rs.getObject("created_at", LocalDateTime.class));
    }
}
