package com.pbojava.controller;

import com.pbojava.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Halaman uji untuk memastikan Servlet + JSP + JSTL + database sudah jalan.
 * Boleh dihapus setelah halaman asli dibuat.
 */
@WebServlet("/tes")
public class TesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sql = "SELECT kategori, nama_fitur, harga_dasar FROM master_features ORDER BY id";
        List<Map<String, Object>> daftarFitur = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> fitur = new LinkedHashMap<>();
                fitur.put("kategori", rs.getString("kategori"));
                fitur.put("nama", rs.getString("nama_fitur"));
                fitur.put("harga", rs.getBigDecimal("harga_dasar"));
                daftarFitur.add(fitur);
            }
        } catch (SQLException e) {
            request.setAttribute("error", e.getMessage());
        }

        request.setAttribute("daftarFitur", daftarFitur);
        request.getRequestDispatcher("/WEB-INF/views/tes.jsp").forward(request, response);
    }
}
