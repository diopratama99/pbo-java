package com.pbojava.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Membuat koneksi ke database berdasarkan file db.properties di classpath.
 * Setiap pemanggilan getConnection() menghasilkan koneksi baru, jadi tutup
 * koneksinya setelah dipakai (gunakan try-with-resources).
 */
public final class DBConnection {

    private static final Properties CONFIG = loadConfig();

    private DBConnection() {
    }

    private static Properties loadConfig() {
        try (InputStream in = DBConnection.class.getResourceAsStream("/db.properties")) {
            if (in == null) {
                throw new IllegalStateException(
                        "File db.properties tidak ditemukan. Salin db.properties.example menjadi db.properties lalu isi password.");
            }
            Properties props = new Properties();
            props.load(in);

            // Di dalam Tomcat, driver JDBC kadang tidak terdaftar otomatis
            Class.forName("com.mysql.cj.jdbc.Driver");
            return props;
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Gagal menyiapkan koneksi database", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                CONFIG.getProperty("db.url"),
                CONFIG.getProperty("db.user"),
                CONFIG.getProperty("db.password"));
    }
}
