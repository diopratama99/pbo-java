package com.pbojava.dao.jdbc;

import com.pbojava.dao.DataAccessException;
import com.pbojava.dao.TeamDAO;
import com.pbojava.model.LocalizedText;
import com.pbojava.model.TeamMember;
import com.pbojava.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Anggota tim dari tabel team_members dan pengalamannya dari team_experiences. */
public class JdbcTeamDAO implements TeamDAO {

    @Override
    public List<TeamMember> findAll() {
        String sqlAnggota = "SELECT id, nama, foto, posisi, posisi_en, pendidikan, pendidikan_en "
                + "FROM team_members ORDER BY urutan, id";
        String sqlPengalaman = "SELECT member_id, deskripsi, deskripsi_en FROM team_experiences ORDER BY id";

        try (Connection conn = DBConnection.getConnection()) {
            // Ambil semua pengalaman sekali saja, lalu dikelompokkan per anggota
            Map<Integer, List<LocalizedText>> pengalaman = new HashMap<>();
            try (PreparedStatement stmt = conn.prepareStatement(sqlPengalaman);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pengalaman.computeIfAbsent(rs.getInt("member_id"), key -> new ArrayList<>())
                            .add(JdbcUtil.text(rs.getString("deskripsi"), rs.getString("deskripsi_en")));
                }
            }

            List<TeamMember> hasil = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement(sqlAnggota);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    hasil.add(new TeamMember(rs.getString("nama"), rs.getString("foto"),
                            JdbcUtil.text(rs.getString("posisi"), rs.getString("posisi_en")),
                            JdbcUtil.text(rs.getString("pendidikan"), rs.getString("pendidikan_en")),
                            pengalaman.getOrDefault(rs.getInt("id"), List.of())));
                }
            }
            return hasil;
        } catch (SQLException e) {
            throw new DataAccessException("Gagal membaca data tim", e);
        }
    }
}
