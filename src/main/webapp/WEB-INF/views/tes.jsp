<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Tes Koneksi</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container py-5">
    <h1 class="h3 mb-4">Tes Servlet + JSP + Database</h1>

    <c:choose>
        <c:when test="${not empty error}">
            <div class="alert alert-danger">Koneksi database gagal: ${error}</div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-success">Semua jalan. Ada ${daftarFitur.size()} fitur di database.</div>
            <table class="table table-striped">
                <thead>
                    <tr><th>Kategori</th><th>Nama Fitur</th><th class="text-end">Harga Dasar</th></tr>
                </thead>
                <tbody>
                    <c:forEach var="fitur" items="${daftarFitur}">
                        <tr>
                            <td>${fitur.kategori}</td>
                            <td>${fitur.nama}</td>
                            <td class="text-end">Rp <fmt:formatNumber value="${fitur.harga}" pattern="#,##0"/></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</body>
</html>
