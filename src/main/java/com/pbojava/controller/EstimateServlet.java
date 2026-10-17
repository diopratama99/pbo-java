package com.pbojava.controller;

import com.pbojava.dao.DAOFactory;
import com.pbojava.model.feature.Feature;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/** Kalkulator estimasi + form pengajuan, dan halaman pengajuan berhasil. */
@WebServlet({"/estimate", "/estimate/success"})
public class EstimateServlet extends HttpServlet {

    /** Prototype: kode tiket masih tetap. Pembuatan kode tiket sungguhan dibuat di tahap berikutnya. */
    private static final String CONTOH_TIKET = "DA-7K2P9Q";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getServletPath().equals("/estimate/success")) {
            showSuccess(request, response);
            return;
        }

        List<Feature> features = DAOFactory.featureDAO().findActive();
        request.setAttribute("platforms", byKategori(features, "PLATFORM"));
        request.setAttribute("databases", byKategori(features, "DATABASE"));
        request.setAttribute("cores", byKategori(features, "CORE"));
        request.setAttribute("integrations", byKategori(features, "INTEGRATION"));
        request.getRequestDispatcher("/WEB-INF/views/client/estimate.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Prototype: data belum disimpan dan harga belum dihitung ulang di server
        response.sendRedirect(request.getContextPath() + "/estimate/success?ticket=" + CONTOH_TIKET);
    }

    private void showSuccess(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String ticket = request.getParameter("ticket");
        if (ticket == null || ticket.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/estimate");
            return;
        }
        request.setAttribute("ticket", ticket);
        request.getRequestDispatcher("/WEB-INF/views/client/success.jsp").forward(request, response);
    }

    private static List<Feature> byKategori(List<Feature> features, String kategori) {
        return features.stream().filter(feature -> feature.getKategori().equals(kategori)).toList();
    }
}
