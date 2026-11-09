package com.pbojava.controller;

import com.pbojava.dao.DAOFactory;
import com.pbojava.dao.ProjectRequestDAO;
import com.pbojava.exception.InvalidStatusException;
import com.pbojava.exception.ValidationException;
import com.pbojava.model.ProjectRequest;
import com.pbojava.model.RequestStatus;
import com.pbojava.pricing.QuotationCalculator;
import com.pbojava.service.RequestService;

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

/** Daftar pengajuan (dengan filter status), detail pengajuan, dan aksi review/setujui/tolak. */
@WebServlet("/admin/requests")
public class AdminRequestServlet extends HttpServlet {

    /** Kode error di URL (?error=...) → key pesan. Dibatasi supaya isi URL tidak bisa dipakai sebagai key bebas. */
    private static final Map<String, String> ERRORS = Map.of(
            "status", "admin.error.status",
            "wag", "validation.wag",
            "reason", "admin.error.reason");

    private final RequestService service = new RequestService(
            DAOFactory.featureDAO(), DAOFactory.projectRequestDAO(), QuotationCalculator.standar());

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
            request.setAttribute("errorKey", ERRORS.get(request.getParameter("error")));
            request.getRequestDispatcher("/WEB-INF/views/admin/request-detail.jsp").forward(request, response);
            return;
        }

        RequestStatus status = parseStatus(request.getParameter("status"));
        List<ProjectRequest> items = status == null ? dao.findAll() : dao.findByStatus(status);

        // Jumlah pengajuan per status untuk ditampilkan di tombol filter
        Map<String, Integer> counts = new LinkedHashMap<>();
        int total = 0;
        for (Map.Entry<RequestStatus, Integer> entry : dao.countByStatus().entrySet()) {
            counts.put(entry.getKey().name(), entry.getValue());
            total += entry.getValue();
        }

        request.setAttribute("items", items);
        request.setAttribute("activeStatus", status == null ? "" : status.name());
        request.setAttribute("statuses", RequestStatus.values());
        request.setAttribute("counts", counts);
        request.setAttribute("totalCount", total);
        request.getRequestDispatcher("/WEB-INF/views/admin/requests.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer id = parseId(request.getParameter("id"));
        if (id == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String action = request.getParameter("action");
        String nilai = "approve".equals(action) ? request.getParameter("wagLink") : request.getParameter("alasan");
        String detailUrl = request.getContextPath() + "/admin/requests?id=" + id;

        try {
            if (!service.ubahStatus(id, action, nilai)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            response.sendRedirect(detailUrl);
        } catch (InvalidStatusException e) {
            response.sendRedirect(detailUrl + "&error=status");
        } catch (ValidationException e) {
            response.sendRedirect(detailUrl + ("validation.wag".equals(e.getMessageKey()) ? "&error=wag" : "&error=reason"));
        }
    }

    private static Integer parseId(String value) {
        try {
            return value == null ? null : Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static RequestStatus parseStatus(String value) {
        try {
            return value == null || value.isBlank() ? null : RequestStatus.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
