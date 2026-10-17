<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:admin-layout titleKey="admin.requests.title" active="requests">

    <a href="${ctx}/admin/requests" class="text-sm text-muted transition hover:text-ink">&larr; <fmt:message key="admin.detail.back"/></a>

    <div class="mt-4 flex flex-wrap items-center gap-4">
        <h1 class="text-3xl font-semibold tracking-tight md:text-5xl"><c:out value="${item.kodeTiket}"/></h1>
        <t:status-badge status="${item.status}"/>
    </div>
    <p class="mt-2 text-sm text-muted">${item.formatTanggal(sessionScope.lang)}</p>

    <div class="mt-8 grid gap-6 xl:grid-cols-[1fr_340px]">
        <div class="space-y-6">
            <%-- Data klien --%>
            <section class="panel">
                <h2 class="label text-muted"><fmt:message key="admin.detail.client"/></h2>
                <dl class="mt-5 grid gap-5 text-sm sm:grid-cols-2">
                    <div>
                        <dt class="text-muted"><fmt:message key="estimate.form.name"/></dt>
                        <dd class="mt-1 font-medium"><c:out value="${item.client.nama}"/></dd>
                    </div>
                    <div>
                        <dt class="text-muted"><fmt:message key="estimate.form.company"/></dt>
                        <dd class="mt-1 font-medium">
                            <c:choose>
                                <c:when test="${not empty item.client.instansi}"><c:out value="${item.client.instansi}"/></c:when>
                                <c:otherwise><span class="text-muted"><fmt:message key="admin.detail.noCompany"/></span></c:otherwise>
                            </c:choose>
                        </dd>
                    </div>
                    <div>
                        <dt class="text-muted"><fmt:message key="estimate.form.whatsapp"/></dt>
                        <dd class="mt-1 font-medium"><c:out value="${item.client.noWa}"/></dd>
                    </div>
                    <div>
                        <dt class="text-muted"><fmt:message key="estimate.form.email"/></dt>
                        <dd class="mt-1 font-medium break-all"><c:out value="${item.client.email}"/></dd>
                    </div>
                </dl>
            </section>

            <%-- Fitur dan harga --%>
            <section class="panel">
                <div class="flex flex-wrap items-baseline justify-between gap-2">
                    <h2 class="label text-muted"><fmt:message key="admin.detail.features"/></h2>
                    <p class="text-xs text-muted/70"><fmt:message key="admin.detail.priceNote"/></p>
                </div>
                <ul class="mt-5 divide-y divide-ink/5 text-sm">
                    <c:forEach var="line" items="${item.fitur}">
                        <li class="flex items-center justify-between gap-4 py-3">
                            <div>
                                <p class="font-medium"><c:out value="${line.feature.nama.get(sessionScope.lang)}"/></p>
                                <p class="text-xs text-muted"><fmt:message key="category.${line.feature.kategori}"/></p>
                            </div>
                            <p class="shrink-0 tabular-nums"><t:rupiah value="${line.hargaSaatItu}"/></p>
                        </li>
                    </c:forEach>
                </ul>
                <dl class="mt-2 space-y-2 border-t border-ink/10 pt-5 text-sm">
                    <c:if test="${item.diskon > 0}">
                        <div class="flex justify-between gap-4 text-muted">
                            <dt><fmt:message key="common.subtotal"/></dt>
                            <dd class="tabular-nums"><t:rupiah value="${item.subtotal}"/></dd>
                        </div>
                        <div class="flex justify-between gap-4 text-glacier-deep">
                            <dt><fmt:message key="common.discount"/></dt>
                            <dd class="tabular-nums">- <t:rupiah value="${item.diskon}"/></dd>
                        </div>
                    </c:if>
                    <div class="flex items-baseline justify-between gap-4 pt-1">
                        <dt class="font-medium"><fmt:message key="common.total"/></dt>
                        <dd class="text-2xl font-semibold tracking-tight tabular-nums"><t:rupiah value="${item.totalEstimasi}"/></dd>
                    </div>
                </dl>
            </section>

            <%-- Catatan --%>
            <section class="panel">
                <h2 class="label text-muted"><fmt:message key="admin.detail.notes"/></h2>
                <c:choose>
                    <c:when test="${not empty item.customNotes}">
                        <p class="mt-4 text-sm leading-relaxed text-ink/80"><c:out value="${item.customNotes}"/></p>
                    </c:when>
                    <c:otherwise>
                        <p class="mt-4 text-sm text-muted"><fmt:message key="admin.detail.noNotes"/></p>
                    </c:otherwise>
                </c:choose>
            </section>
        </div>

        <%-- Aksi sesuai status. Prototype: tombol belum mengubah data --%>
        <aside class="xl:sticky xl:top-6 xl:self-start">
            <section class="panel space-y-6">
                <h2 class="label text-muted"><fmt:message key="admin.detail.actions"/></h2>

                <c:choose>
                    <c:when test="${item.status == 'PENDING'}">
                        <form method="post" action="${ctx}/admin/requests">
                            <input type="hidden" name="id" value="${item.id}">
                            <input type="hidden" name="action" value="review">
                            <button type="submit" class="btn btn-primary w-full"><fmt:message key="admin.detail.startReview"/></button>
                        </form>
                    </c:when>
                    <c:when test="${item.status == 'REVIEW'}">
                        <form method="post" action="${ctx}/admin/requests" data-validate class="space-y-3">
                            <input type="hidden" name="id" value="${item.id}">
                            <input type="hidden" name="action" value="approve">
                            <label class="block">
                                <span class="field-label"><fmt:message key="admin.detail.wagLink"/></span>
                                <input type="url" name="wagLink" class="input" required
                                       pattern="https://chat\.whatsapp\.com/.+" placeholder="https://chat.whatsapp.com/..."
                                       data-msg-pattern="<fmt:message key="validation.wag"/>">
                                <span class="mt-2 block px-2 text-xs text-muted/70"><fmt:message key="admin.detail.wagHint"/></span>
                            </label>
                            <button type="submit" class="btn btn-primary w-full"><fmt:message key="admin.detail.approve"/></button>
                        </form>
                    </c:when>
                    <c:when test="${item.status == 'APPROVED'}">
                        <div>
                            <p class="field-label"><fmt:message key="admin.detail.wagLink"/></p>
                            <a href="<c:out value="${item.wagLink}"/>" target="_blank" rel="noopener noreferrer"
                               class="text-sm break-all text-glacier-deep underline-offset-4 hover:underline"><c:out value="${item.wagLink}"/></a>
                        </div>
                        <p class="text-xs leading-relaxed text-muted"><fmt:message key="admin.detail.final"/></p>
                    </c:when>
                    <c:otherwise>
                        <div>
                            <p class="field-label"><fmt:message key="admin.detail.reason"/></p>
                            <p class="text-sm leading-relaxed text-ink/80"><c:out value="${item.alasanDitolak}"/></p>
                        </div>
                        <p class="text-xs leading-relaxed text-muted"><fmt:message key="admin.detail.final"/></p>
                    </c:otherwise>
                </c:choose>

                <%-- Tolak bisa dari PENDING maupun REVIEW --%>
                <c:if test="${item.status == 'PENDING' or item.status == 'REVIEW'}">
                    <form method="post" action="${ctx}/admin/requests" data-validate class="space-y-3 border-t border-ink/10 pt-6">
                        <input type="hidden" name="id" value="${item.id}">
                        <input type="hidden" name="action" value="reject">
                        <label class="block">
                            <span class="field-label"><fmt:message key="admin.detail.reason"/></span>
                            <textarea name="alasan" rows="3" class="textarea" required maxlength="255"></textarea>
                        </label>
                        <button type="submit" class="btn btn-danger w-full"><fmt:message key="admin.detail.reject"/></button>
                    </form>
                </c:if>
            </section>
        </aside>
    </div>

</t:admin-layout>
