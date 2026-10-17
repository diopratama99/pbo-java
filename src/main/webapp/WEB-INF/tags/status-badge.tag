<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" description="Pill badge status pengajuan" %>
<%@ attribute name="status" required="true" type="java.lang.Object" description="RequestStatus atau teks PENDING/REVIEW/APPROVED/REJECTED" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="s" value="${status}"/>
<c:choose>
    <c:when test="${s == 'APPROVED'}"><span class="badge bg-glacier text-ink"><span class="size-1.5 rounded-full bg-glacier-deep"></span></c:when>
    <c:when test="${s == 'REVIEW'}"><span class="badge bg-lavender text-ink"><span class="size-1.5 rounded-full bg-ink/40"></span></c:when>
    <c:when test="${s == 'REJECTED'}"><span class="badge bg-danger/10 text-danger"><span class="size-1.5 rounded-full bg-danger"></span></c:when>
    <c:otherwise><span class="badge bg-ink/5 text-muted"><span class="size-1.5 rounded-full bg-muted/50"></span></c:otherwise>
</c:choose>
<fmt:message key="status.${s}"/></span>
