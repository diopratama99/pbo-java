<%@ tag pageEncoding="UTF-8" description="Layout halaman admin: style terang yang sama dengan klien, tanpa blob dan grain supaya tabel mudah dibaca" %>
<%@ attribute name="titleKey" required="true" %>
<%@ attribute name="active" required="false" description="Menu yang aktif: requests, portfolios, atau features" %>
<%@ attribute name="bare" required="false" type="java.lang.Boolean" description="true untuk halaman tanpa menu (login)" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="robots" content="noindex">
    <title><fmt:message key="${titleKey}"/> | <fmt:message key="admin.label"/> <fmt:message key="app.name"/></title>
    <link rel="preload" href="${ctx}/assets/fonts/outfit/OutfitVariable-latin.woff2" as="font" type="font/woff2" crossorigin>
    <link rel="stylesheet" href="${ctx}/assets/css/app.css">
    <script src="${ctx}/assets/js/validate.js" defer></script>
    <script src="${ctx}/assets/js/select.js" defer></script>
    <t:validation-messages/>
</head>
<body class="min-h-screen">

<%-- Tombol ID / EN --%>
<c:set var="langToggle">
    <div class="inline-flex rounded-full border border-ink/10 p-1 text-xs font-semibold" role="group" aria-label="<fmt:message key="nav.lang"/>">
        <a href="${langUrlId}" class="rounded-full px-3 py-1.5 transition ${sessionScope.lang == 'id' ? 'bg-ink text-cream' : 'text-muted hover:text-ink'}" hreflang="id" lang="id">ID</a>
        <a href="${langUrlEn}" class="rounded-full px-3 py-1.5 transition ${sessionScope.lang == 'en' ? 'bg-ink text-cream' : 'text-muted hover:text-ink'}" hreflang="en" lang="en">EN</a>
    </div>
</c:set>

<c:choose>
    <c:when test="${bare}">
        <main class="grid min-h-screen place-items-center px-5 py-10">
            <div class="w-full max-w-sm">
                <jsp:doBody/>
                <div class="mt-6 flex items-center justify-between">
                    <a href="${ctx}/" class="text-xs text-muted transition hover:text-ink"><fmt:message key="admin.nav.site"/> &rarr;</a>
                    ${langToggle}
                </div>
            </div>
        </main>
    </c:when>
    <c:otherwise>
        <c:set var="menu">
            <li><a href="${ctx}/admin/requests" class="block rounded-full px-4 py-2.5 text-sm font-medium transition ${active == 'requests' ? 'bg-glacier text-ink' : 'text-muted hover:bg-sage hover:text-ink'}"><fmt:message key="admin.nav.requests"/></a></li>
            <li><a href="${ctx}/admin/portfolios" class="block rounded-full px-4 py-2.5 text-sm font-medium transition ${active == 'portfolios' ? 'bg-glacier text-ink' : 'text-muted hover:bg-sage hover:text-ink'}"><fmt:message key="admin.nav.portfolios"/></a></li>
            <li><a href="${ctx}/admin/features" class="block rounded-full px-4 py-2.5 text-sm font-medium transition ${active == 'features' ? 'bg-glacier text-ink' : 'text-muted hover:bg-sage hover:text-ink'}"><fmt:message key="admin.nav.features"/></a></li>
        </c:set>

        <div class="mx-auto flex max-w-7xl flex-col gap-6 px-4 py-4 md:px-8 md:py-6 lg:flex-row lg:gap-8">
            <%-- Sidebar laptop --%>
            <aside class="hidden w-60 shrink-0 lg:block">
                <div class="panel sticky top-6 flex min-h-[calc(100vh-3rem)] flex-col p-4 md:p-4">
                    <div class="px-4 pt-3 pb-6">
                        <a href="${ctx}/admin/requests" class="text-2xl font-semibold tracking-tight"><fmt:message key="app.name"/></a>
                        <span class="badge ml-2 bg-ink/5 text-muted"><fmt:message key="admin.label"/></span>
                    </div>
                    <ul class="space-y-1">${menu}</ul>
                    <div class="mt-auto space-y-4 px-4 pb-2">
                        <c:if test="${not empty sessionScope.admin}">
                            <p class="text-xs text-muted"><c:out value="${sessionScope.admin.nama}"/></p>
                        </c:if>
                        ${langToggle}
                        <div class="flex flex-col gap-2 text-xs">
                            <a href="${ctx}/" class="text-muted transition hover:text-ink"><fmt:message key="admin.nav.site"/> &rarr;</a>
                            <a href="${ctx}/admin/logout" class="text-muted transition hover:text-danger"><fmt:message key="admin.nav.logout"/></a>
                        </div>
                    </div>
                </div>
            </aside>

            <%-- Bar atas HP dan tablet --%>
            <header class="panel flex items-center justify-between gap-3 px-5 py-3 md:px-5 md:py-3 lg:hidden">
                <a href="${ctx}/admin/requests" class="text-xl font-semibold tracking-tight"><fmt:message key="app.name"/></a>
                <div class="flex items-center gap-2">
                    ${langToggle}
                    <details class="relative">
                        <summary class="btn btn-primary btn-sm cursor-pointer list-none [&::-webkit-details-marker]:hidden"><fmt:message key="nav.menu"/></summary>
                        <div class="absolute right-0 z-20 mt-3 w-60 rounded-card border border-ink/5 bg-white p-2 shadow-2xl">
                            <ul class="space-y-1">${menu}</ul>
                            <div class="mt-2 flex justify-between border-t border-ink/10 px-4 pt-3 pb-2 text-xs">
                                <a href="${ctx}/" class="text-muted hover:text-ink"><fmt:message key="admin.nav.site"/></a>
                                <a href="${ctx}/admin/logout" class="text-muted hover:text-danger"><fmt:message key="admin.nav.logout"/></a>
                            </div>
                        </div>
                    </details>
                </div>
            </header>

            <main class="min-w-0 flex-1 pb-10 lg:pt-4">
                <jsp:doBody/>
            </main>
        </div>
    </c:otherwise>
</c:choose>

<script src="${ctx}/assets/js/admin.js" defer></script>
</body>
</html>
