package com.pbojava.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Program kecil untuk memastikan koneksi ke database berhasil.
 * Jalankan: mvnw compile exec:java -Dexec.mainClass=com.pbojava.util.TesKoneksi
 */
public class TesKoneksi {

    public static void main(String[] args) {
        String sql = "SELECT kategori, COUNT(*) AS jumlah FROM master_features GROUP BY kategori";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Koneksi berhasil ke: " + conn.getMetaData().getURL());
            System.out.println("Jumlah fitur per kategori:");
            while (rs.next()) {
                System.out.printf("  %-12s %d%n", rs.getString("kategori"), rs.getInt("jumlah"));
            }
        } catch (SQLException e) {
            System.err.println("Koneksi gagal: " + e.getMessage());
            System.err.println("Cek: apakah SSH tunnel sudah dibuka dan isi db.properties sudah benar?");
        }
    }
}
