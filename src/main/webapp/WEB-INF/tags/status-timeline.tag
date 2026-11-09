<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" description="Timeline riwayat status pengajuan, dari yang paling lama" %>
<%@ attribute name="items" required="true" type="java.util.List" description="List of StatusHistory" %>
<%@ attribute name="showAdmin" required="false" type="java.lang.Boolean" description="Tampilkan nama admin. Hanya untuk halaman admin" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<ol class="space-y-5 border-l border-ink/10 pl-5">
    <c:forEach var="h" items="${items}">
        <li class="relative">
            <%-- Warna titik sama dengan status-badge.tag --%>
            <span class="absolute top-1.5 -left-[25px] size-2.5 rounded-full ring-4 ring-white
                ${h.status == 'APPROVED' ? 'bg-glacier-deep' : h.status == 'REJECTED' ? 'bg-danger' : h.status == 'REVIEW' ? 'bg-ink/40' : 'bg-muted/50'}"
                  aria-hidden="true"></span>
            <p class="text-sm font-medium"><fmt:message key="history.${h.status}"/></p>
            <p class="mt-0.5 text-xs text-muted">
                ${h.formatWaktu(sessionScope.lang)}
                <c:if test="${showAdmin and not empty h.adminNama}">
                    <%-- &#32; = spasi. Spasi biasa di antara dua tag dibuang saat halaman dirender --%>
                    &middot; <fmt:message key="history.by"/>&#32;<c:out value="${h.adminNama}"/>
                </c:if>
            </p>
        </li>
    </c:forEach>
</ol>
