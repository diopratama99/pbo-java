package com.pbojava.controller;

import com.pbojava.dao.DAOFactory;
import com.pbojava.model.feature.Feature;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/** Landing page + etalase portofolio. URL "" artinya tepat di root aplikasi (/pbo-java/). */
@WebServlet("")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Feature> features = DAOFactory.featureDAO().findActive();
        // Harga "Mulai dari": platform termurah + database termurah
        request.setAttribute("startingPrice", termurah(features, "PLATFORM").add(termurah(features, "DATABASE")));
        request.setAttribute("portfolios", DAOFactory.portfolioDAO().findAll());
        request.getRequestDispatcher("/WEB-INF/views/client/home.jsp").forward(request, response);
    }

    private static BigDecimal termurah(List<Feature> features, String kategori) {
        return features.stream()
                .filter(feature -> feature.getKategori().equals(kategori))
                .map(Feature::getHargaDasar)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }
}
