package com.pbojava.controller;

import com.pbojava.dao.DAOFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Landing page + etalase portofolio. URL "" artinya tepat di root aplikasi (/pbo-java/). */
@WebServlet("")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("portfolios", DAOFactory.portfolioDAO().findAll());
        request.getRequestDispatcher("/WEB-INF/views/client/home.jsp").forward(request, response);
    }
}
