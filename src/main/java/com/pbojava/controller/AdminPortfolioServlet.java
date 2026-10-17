package com.pbojava.controller;

import com.pbojava.dao.DAOFactory;

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
        request.setAttribute("items", DAOFactory.portfolioDAO().findAll());

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
        request.getRequestDispatcher("/WEB-INF/views/admin/portfolios.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Prototype: simpan dan hapus belum mengubah data
        response.sendRedirect(request.getContextPath() + "/admin/portfolios");
    }
}
