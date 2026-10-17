<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" description="Format angka jadi Rupiah: Rp 14.350.000 (id) atau Rp 14,350,000 (en)" %>
<%@ attribute name="value" required="true" type="java.lang.Object" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
Rp <fmt:formatNumber value="${value}" pattern="#,##0"/>
