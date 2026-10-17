package com.pbojava.controller;

import com.pbojava.dao.DAOFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Cek status pengajuan pakai kode tiket + email. */
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
            DAOFactory.projectRequestDAO()
                    .findByTicketAndEmail(ticket, email)
                    .ifPresent(found -> request.setAttribute("result", found));
        }
        request.getRequestDispatcher("/WEB-INF/views/client/track.jsp").forward(request, response);
    }

    private static String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
