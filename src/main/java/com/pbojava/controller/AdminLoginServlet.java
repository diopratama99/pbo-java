package com.pbojava.controller;

import com.pbojava.dao.DAOFactory;
import com.pbojava.model.Admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

/**
 * Login dan logout admin.
 * Prototype: belum ada filter penjaga /admin/*, jadi halaman admin masih bisa dibuka tanpa login.
 */
@WebServlet({"/admin", "/admin/login", "/admin/logout"})
public class AdminLoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String ctx = request.getContextPath();
        switch (request.getServletPath()) {
            case "/admin" -> response.sendRedirect(ctx + "/admin/requests");
            case "/admin/logout" -> {
                request.getSession().removeAttribute("admin");
                response.sendRedirect(ctx + "/admin/login");
            }
            default -> request.getRequestDispatcher("/WEB-INF/views/admin/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Optional<Admin> admin = DAOFactory.adminDAO().findByUsername(username == null ? "" : username.trim());
        if (admin.isPresent() && admin.get().passwordCocok(password == null ? "" : password)) {
            request.getSession().setAttribute("admin", admin.get());
            response.sendRedirect(request.getContextPath() + "/admin/requests");
            return;
        }

        request.setAttribute("username", username);
        request.setAttribute("loginFailed", true);
        request.getRequestDispatcher("/WEB-INF/views/admin/login.jsp").forward(request, response);
    }
}
