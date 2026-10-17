package com.pbojava.controller;

import com.pbojava.dao.DAOFactory;
import com.pbojava.dao.ProjectRequestDAO;
import com.pbojava.model.ProjectRequest;
import com.pbojava.model.RequestStatus;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Daftar pengajuan (dengan filter status) dan detail pengajuan. */
@WebServlet("/admin/requests")
public class AdminRequestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ProjectRequestDAO dao = DAOFactory.projectRequestDAO();

        Integer id = parseId(request.getParameter("id"));
        if (id != null) {
            Optional<ProjectRequest> found = dao.findById(id);
            if (found.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            request.setAttribute("item", found.get());
            request.getRequestDispatcher("/WEB-INF/views/admin/request-detail.jsp").forward(request, response);
            return;
        }

        RequestStatus status = parseStatus(request.getParameter("status"));
        List<ProjectRequest> items = status == null ? dao.findAll() : dao.findByStatus(status);

        // Jumlah pengajuan per status untuk ditampilkan di tombol filter
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (RequestStatus s : RequestStatus.values()) {
            counts.put(s.name(), dao.findByStatus(s).size());
        }

        request.setAttribute("items", items);
        request.setAttribute("activeStatus", status == null ? "" : status.name());
        request.setAttribute("statuses", RequestStatus.values());
        request.setAttribute("counts", counts);
        request.setAttribute("totalCount", dao.findAll().size());
        request.getRequestDispatcher("/WEB-INF/views/admin/requests.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Prototype: aksi Review/Setujui/Tolak belum mengubah data, langsung kembali ke halaman detail
        response.sendRedirect(request.getContextPath() + "/admin/requests?id=" + parseIdOrZero(request.getParameter("id")));
    }

    private static Integer parseId(String value) {
        try {
            return value == null ? null : Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static int parseIdOrZero(String value) {
        Integer id = parseId(value);
        return id == null ? 0 : id;
    }

    private static RequestStatus parseStatus(String value) {
        try {
            return value == null || value.isBlank() ? null : RequestStatus.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
