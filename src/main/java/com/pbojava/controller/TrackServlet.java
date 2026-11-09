package com.pbojava.controller;

import com.pbojava.dao.DAOFactory;
import com.pbojava.dao.ProjectRequestDAO;
import com.pbojava.model.ProjectRequest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

/**
 * Cek status pengajuan pakai kode tiket + email.
 * Kalau cocok, ditampilkan juga riwayat statusnya dan pengajuan lain dengan email yang sama.
 */
@WebServlet("/track")
public class TrackServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/client/track.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String ticket = trim(request.getParameter("ticket"));
        String email = trim(request.getParameter("email"));

        request.setAttribute("ticket", ticket);
        request.setAttribute("email", email);
        request.setAttribute("searched", true);

        if (!ticket.isEmpty() && !email.isEmpty()) {
            ProjectRequestDAO dao = DAOFactory.projectRequestDAO();
            Optional<ProjectRequest> found = dao.findByTicketAndEmail(ticket, email);
            if (found.isPresent()) {
                ProjectRequest result = found.get();
                request.setAttribute("result", result);
                request.setAttribute("history", dao.findHistory(result.getId()));
                // Hanya ditampilkan setelah kode tiket dan email terbukti cocok
                request.setAttribute("others", dao.findByEmail(result.getClient().getEmail()).stream()
                        .filter(other -> other.getId() != result.getId())
                        .toList());
            }
        }
        request.getRequestDispatcher("/WEB-INF/views/client/track.jsp").forward(request, response);
    }

    private static String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
