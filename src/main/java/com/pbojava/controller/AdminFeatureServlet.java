package com.pbojava.controller;

import com.pbojava.dao.DAOFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Kelola fitur dan harga. Form tambah/ubah muncul di halaman yang sama:
 * ?new untuk tambah, ?edit=ID untuk ubah.
 */
@WebServlet("/admin/features")
public class AdminFeatureServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("items", DAOFactory.featureDAO().findAll());
        request.setAttribute("kategoriList", new String[] {"PLATFORM", "DATABASE", "CORE", "INTEGRATION"});

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
        request.getRequestDispatcher("/WEB-INF/views/admin/features.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Prototype: simpan dan nonaktifkan belum mengubah data
        response.sendRedirect(request.getContextPath() + "/admin/features");
    }
}
