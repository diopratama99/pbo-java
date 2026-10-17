package com.pbojava.controller;

import com.pbojava.dao.DAOFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Halaman profil tim. */
@WebServlet("/team")
public class TeamServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("members", DAOFactory.teamDAO().findAll());
        request.getRequestDispatcher("/WEB-INF/views/client/team.jsp").forward(request, response);
    }
}
