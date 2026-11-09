package com.pbojava.controller;

import com.pbojava.dao.DAOFactory;
import com.pbojava.exception.ValidationException;
import com.pbojava.model.feature.Feature;
import com.pbojava.pricing.QuotationCalculator;
import com.pbojava.service.PengajuanForm;
import com.pbojava.service.RequestService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Kalkulator estimasi + form pengajuan, dan halaman pengajuan berhasil. */
@WebServlet({"/estimate", "/estimate/success"})
public class EstimateServlet extends HttpServlet {

    private final RequestService service = new RequestService(
            DAOFactory.featureDAO(), DAOFactory.projectRequestDAO(), QuotationCalculator.standar());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getServletPath().equals("/estimate/success")) {
            showSuccess(request, response);
            return;
        }
        showForm(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] features = request.getParameterValues("features");
        PengajuanForm form = new PengajuanForm(
                request.getParameter("platform"),
                request.getParameter("database"),
                features == null ? List.of() : Arrays.asList(features),
                request.getParameter("notes"),
                request.getParameter("nama"),
                request.getParameter("instansi"),
                request.getParameter("noWa"),
                request.getParameter("email"));

        try {
            String ticket = service.kirim(form);
            response.sendRedirect(request.getContextPath() + "/estimate/success?ticket="
                    + URLEncoder.encode(ticket, StandardCharsets.UTF_8));
        } catch (ValidationException e) {
            // Tampilkan form lagi dengan pilihan dan isian yang sama, ditambah pesan error
            List<String> dipilih = new ArrayList<>(form.featureIds());
            dipilih.add(form.platformId());
            dipilih.add(form.databaseId());
            request.setAttribute("selected", toIds(dipilih));
            request.setAttribute("errorKey", e.getMessageKey());
            showForm(request, response);
        }
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Feature> features = DAOFactory.featureDAO().findActive();
        request.setAttribute("platforms", byKategori(features, "PLATFORM"));
        request.setAttribute("databases", byKategori(features, "DATABASE"));
        request.setAttribute("cores", byKategori(features, "CORE"));
        request.setAttribute("integrations", byKategori(features, "INTEGRATION"));
        request.getRequestDispatcher("/WEB-INF/views/client/estimate.jsp").forward(request, response);
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

    /** Id fitur yang dipilih, untuk menandai pilihan di option-card.tag. Isian yang bukan angka diabaikan. */
    private static Set<Integer> toIds(List<String> values) {
        Set<Integer> ids = new HashSet<>();
        for (String value : values) {
            try {
                ids.add(Integer.valueOf(value == null ? "" : value.trim()));
            } catch (NumberFormatException ignored) {
                // abaikan
            }
        }
        return ids;
    }
}
