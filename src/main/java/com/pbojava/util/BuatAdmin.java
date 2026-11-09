package com.pbojava.util;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Membuat akun admin baru, atau mengganti password kalau username-nya sudah ada.
 * Password diketik di terminal (tidak ditulis di argumen, supaya tidak tersimpan di history).
 *
 * Jalankan: mvnw compile exec:java -Dexec.mainClass=com.pbojava.util.BuatAdmin -Dexec.args="admin 'Admin Bisa.'"
 */
public class BuatAdmin {

    private static final int MINIMAL_PASSWORD = 10;

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Pemakaian: BuatAdmin <username> <nama>");
            System.exit(1);
        }
        String username = args[0].trim();
        String nama = args[1].trim();

        String password = bacaPassword();
        if (password.length() < MINIMAL_PASSWORD) {
            System.err.println("Password minimal " + MINIMAL_PASSWORD + " karakter.");
            System.exit(1);
        }

        String salt = PasswordUtil.buatSalt();
        String hash = PasswordUtil.hash(password, salt);
        String sql = "INSERT INTO admins (username, nama, password_hash, salt) VALUES (?, ?, ?, ?) AS baru "
                + "ON DUPLICATE KEY UPDATE nama = baru.nama, password_hash = baru.password_hash, salt = baru.salt";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, nama);
            stmt.setString(3, hash);
            stmt.setString(4, salt);
            stmt.executeUpdate();
            System.out.println("Akun admin '" + username + "' sudah disimpan.");
        } catch (SQLException e) {
            System.err.println("Gagal menyimpan admin: " + e.getMessage());
            System.exit(1);
        }
    }

    /** Pakai Console kalau ada (input tidak terlihat), kalau tidak baca satu baris dari stdin. */
    private static String bacaPassword() throws IOException {
        Console console = System.console();
        if (console != null) {
            char[] input = console.readPassword("Password baru: ");
            return input == null ? "" : new String(input);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        String line = reader.readLine();
        return line == null ? "" : line;
    }
}
