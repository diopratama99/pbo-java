package com.pbojava.controller;

import com.pbojava.dao.DAOFactory;
import com.pbojava.model.LocalizedText;
import com.pbojava.model.feature.CoreFeature;
import com.pbojava.model.feature.DatabaseFeature;
import com.pbojava.model.feature.Feature;
import com.pbojava.model.feature.IntegrationFeature;
import com.pbojava.model.feature.PlatformFeature;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Kelola fitur dan harga. Form tambah/ubah muncul di halaman yang sama:
 * ?new untuk tambah, ?edit=ID untuk ubah.
 */
@WebServlet("/admin/features")
public class AdminFeatureServlet extends HttpServlet {

    private static final BigDecimal PENGALI_MIN = BigDecimal.ONE;
    private static final BigDecimal PENGALI_MAKS = new BigDecimal("9.99");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameter("new") != null) {
            request.setAttribute("formMode", "new");
        } else if (request.getParameter("edit") != null) {
            try {
                int id = Integer.parseInt(request.getParameter("edit"));
                DAOFactory.featureDAO().findById(id).ifPresent(found -> {
                    request.setAttribute("formMode", "edit");
                    request.setAttribute("editing", found);
                });
            } catch (NumberFormatException ignored) {
                // ID tidak valid, tampilkan daftar saja
            }
        }
        showPage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = AdminForms.parseInt(request.getParameter("id"));
        String action = request.getParameter("action");

        if ("deactivate".equals(action) || "activate".equals(action)) {
            DAOFactory.featureDAO().setAktif(id, "activate".equals(action));
            response.sendRedirect(request.getContextPath() + "/admin/features");
            return;
        }

        Optional<Feature> lama = id == 0 ? Optional.empty() : DAOFactory.featureDAO().findById(id);
        // Kategori fitur lama tidak boleh diganti, karena cara hitung harganya ikut berubah
        String kategori = lama.map(Feature::getKategori).orElse(AdminForms.trim(request.getParameter("kategori")));

        String namaId = AdminForms.trim(request.getParameter("namaId"));
        String namaEn = AdminForms.trim(request.getParameter("namaEn"));
        String deskripsiId = AdminForms.trim(request.getParameter("deskripsiId"));
        String deskripsiEn = AdminForms.trim(request.getParameter("deskripsiEn"));
        BigDecimal harga = AdminForms.parseRupiah(request.getParameter("hargaDasar"));
        BigDecimal pengali = AdminForms.parseDecimal(request.getParameter("pengali"));
        BigDecimal biayaSetup = AdminForms.parseRupiah(request.getParameter("biayaSetup"));
        boolean aktif = request.getParameter("aktif") != null;

        boolean valid = (id == 0 || lama.isPresent())
                && !namaId.isEmpty() && namaId.length() <= 100
                && !namaEn.isEmpty() && namaEn.length() <= 100
                && deskripsiId.length() <= 255 && deskripsiEn.length() <= 255
                && harga != null
                && (!kategori.equals("PLATFORM") || pengaliValid(pengali));

        Feature feature = buatFeature(kategori, id, new LocalizedText(namaId, namaEn),
                new LocalizedText(deskripsiId, deskripsiEn), harga, pengali,
                biayaSetup == null ? BigDecimal.ZERO : biayaSetup, aktif);

        if (!valid || feature == null) {
            request.setAttribute("formMode", id == 0 ? "new" : "edit");
            request.setAttribute("editing", feature != null ? feature : lama.orElse(null));
            request.setAttribute("errorKey", "admin.error.feature");
            showPage(request, response);
            return;
        }

        DAOFactory.featureDAO().save(feature);
        response.sendRedirect(request.getContextPath() + "/admin/features");
    }

    private void showPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("items", DAOFactory.featureDAO().findAll());
        request.setAttribute("kategoriList", new String[] {"PLATFORM", "DATABASE", "CORE", "INTEGRATION"});
        request.getRequestDispatcher("/WEB-INF/views/admin/features.jsp").forward(request, response);
    }

    private static boolean pengaliValid(BigDecimal pengali) {
        return pengali != null && pengali.scale() <= 2
                && pengali.compareTo(PENGALI_MIN) >= 0 && pengali.compareTo(PENGALI_MAKS) <= 0;
    }

    /** Object subclass yang sesuai dengan kategori. null kalau kategorinya tidak dikenal. */
    private static Feature buatFeature(String kategori, int id, LocalizedText nama, LocalizedText deskripsi,
                                       BigDecimal harga, BigDecimal pengali, BigDecimal biayaSetup, boolean aktif) {
        return switch (kategori) {
            case "PLATFORM" -> new PlatformFeature(id, nama, deskripsi, harga, pengali, aktif);
            case "DATABASE" -> new DatabaseFeature(id, nama, deskripsi, harga, aktif);
            case "CORE" -> new CoreFeature(id, nama, deskripsi, harga, aktif);
            case "INTEGRATION" -> new IntegrationFeature(id, nama, deskripsi, harga, biayaSetup, aktif);
            default -> null;
        };
    }
}
