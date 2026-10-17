<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:admin-layout titleKey="admin.requests.title" active="requests">

    <h1 class="text-3xl font-semibold tracking-tight md:text-4xl"><fmt:message key="admin.requests.title"/></h1>

    <%-- Filter status --%>
    <nav class="mt-6 flex flex-wrap gap-2">
        <a href="${ctx}/admin/requests"
           class="inline-flex items-center gap-2 rounded-full px-4 py-2 text-sm font-medium transition ${empty activeStatus ? 'bg-glacier text-ink' : 'bg-white text-muted shadow-soft hover:text-ink'}">
            <fmt:message key="admin.requests.all"/> <span class="opacity-60">${totalCount}</span>
        </a>
        <c:forEach var="s" items="${statuses}">
            <a href="${ctx}/admin/requests?status=${s}"
               class="inline-flex items-center gap-2 rounded-full px-4 py-2 text-sm font-medium transition ${activeStatus == s.name() ? 'bg-glacier text-ink' : 'bg-white text-muted shadow-soft hover:text-ink'}">
                <fmt:message key="status.${s}"/> <span class="opacity-60">${counts[s.name()]}</span>
            </a>
        </c:forEach>
    </nav>

    <div class="panel relative mt-6 overflow-x-auto p-0 md:p-0">
        <c:choose>
            <c:when test="${empty items}">
                <p class="p-8 text-sm text-muted"><fmt:message key="admin.requests.empty"/></p>
            </c:when>
            <c:otherwise>
                <table class="w-full min-w-[720px] text-left text-sm">
                    <thead>
                        <tr class="label text-muted">
                            <th class="px-6 py-5 font-semibold"><fmt:message key="admin.col.ticket"/></th>
                            <th class="px-6 py-5 font-semibold"><fmt:message key="admin.col.client"/></th>
                            <th class="px-6 py-5 text-right font-semibold"><fmt:message key="admin.col.total"/></th>
                            <th class="px-6 py-5 font-semibold"><fmt:message key="admin.col.status"/></th>
                            <th class="px-6 py-5 font-semibold"><fmt:message key="admin.col.date"/></th>
                            <th class="px-6 py-5"><span class="sr-only"><fmt:message key="admin.open"/></span></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${items}">
                            <tr class="border-t border-ink/5 transition hover:bg-cream">
                                <td class="px-6 py-4 font-semibold tracking-wide"><c:out value="${item.kodeTiket}"/></td>
                                <td class="px-6 py-4">
                                    <p class="font-medium"><c:out value="${item.client.nama}"/></p>
                                    <p class="text-xs text-muted"><c:out value="${item.client.email}"/></p>
                                </td>
                                <td class="px-6 py-4 text-right font-medium tabular-nums"><t:rupiah value="${item.totalEstimasi}"/></td>
                                <td class="px-6 py-4"><t:status-badge status="${item.status}"/></td>
                                <td class="px-6 py-4 text-muted">${item.formatTanggal(sessionScope.lang)}</td>
                                <td class="px-6 py-4 text-right">
                                    <a href="${ctx}/admin/requests?id=${item.id}" class="btn btn-outline btn-sm"><fmt:message key="admin.open"/></a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>

</t:admin-layout>
