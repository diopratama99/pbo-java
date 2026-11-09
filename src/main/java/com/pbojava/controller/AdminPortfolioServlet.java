package com.pbojava.controller;

import com.pbojava.dao.DAOFactory;
import com.pbojava.model.LocalizedText;
import com.pbojava.model.Portfolio;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Kelola portofolio. Form tambah/ubah muncul di halaman yang sama:
 * ?new untuk tambah, ?edit=ID untuk ubah.
 */
@WebServlet("/admin/portfolios")
public class AdminPortfolioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameter("new") != null) {
            request.setAttribute("formMode", "new");
        } else if (request.getParameter("edit") != null) {
            try {
                int id = Integer.parseInt(request.getParameter("edit"));
                DAOFactory.portfolioDAO().findById(id).ifPresent(found -> {
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

        if ("delete".equals(request.getParameter("action"))) {
            DAOFactory.portfolioDAO().delete(id);
            response.sendRedirect(request.getContextPath() + "/admin/portfolios");
            return;
        }

        String judulId = AdminForms.trim(request.getParameter("judulId"));
        String judulEn = AdminForms.trim(request.getParameter("judulEn"));
        String deskripsiId = AdminForms.trim(request.getParameter("deskripsiId"));
        String deskripsiEn = AdminForms.trim(request.getParameter("deskripsiEn"));
        String techStack = AdminForms.trim(request.getParameter("techStack"));
        String imageUrl = AdminForms.trim(request.getParameter("imageUrl"));
        Portfolio portfolio = new Portfolio(id, new LocalizedText(judulId, judulEn),
                new LocalizedText(deskripsiId, deskripsiEn), techStack, imageUrl);

        boolean valid = !judulId.isEmpty() && judulId.length() <= 150
                && !judulEn.isEmpty() && judulEn.length() <= 150
                && deskripsiId.length() <= 2000 && deskripsiEn.length() <= 2000
                && techStack.length() <= 255
                // Gambar ditampilkan sebagai ${ctx}${imageUrl}, jadi harus path di dalam aplikasi
                && imageUrl.startsWith("/") && !imageUrl.startsWith("//") && imageUrl.length() <= 255
                && (id == 0 || DAOFactory.portfolioDAO().findById(id).isPresent());

        if (!valid) {
            request.setAttribute("formMode", id == 0 ? "new" : "edit");
            request.setAttribute("editing", portfolio);
            request.setAttribute("errorKey", "admin.error.portfolio");
            showPage(request, response);
            return;
        }

        DAOFactory.portfolioDAO().save(portfolio);
        response.sendRedirect(request.getContextPath() + "/admin/portfolios");
    }

    private void showPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("items", DAOFactory.portfolioDAO().findAll());
        request.getRequestDispatcher("/WEB-INF/views/admin/portfolios.jsp").forward(request, response);
    }
}
