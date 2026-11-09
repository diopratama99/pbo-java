package com.pbojava.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/** Halaman /admin/* hanya bisa dibuka setelah login. Halaman login sendiri tetap terbuka. */
@WebFilter({"/admin", "/admin/*"})
public class AdminAuthFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request.getServletPath().equals("/admin/login")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        // Halaman admin berisi data klien, jangan disimpan di cache browser
        response.setHeader("Cache-Control", "no-store");
        chain.doFilter(request, response);
    }
}
