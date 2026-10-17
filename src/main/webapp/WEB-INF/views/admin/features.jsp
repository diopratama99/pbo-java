<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:admin-layout titleKey="admin.features.title" active="features">

    <div class="flex flex-wrap items-center justify-between gap-4">
        <div>
            <h1 class="text-3xl font-semibold tracking-tight md:text-4xl"><fmt:message key="admin.features.title"/></h1>
            <p class="mt-2 max-w-xl text-sm text-muted"><fmt:message key="admin.features.note"/></p>
        </div>
        <c:if test="${empty formMode}">
            <a href="${ctx}/admin/features?new" class="btn btn-primary"><fmt:message key="admin.features.add"/></a>
        </c:if>
    </div>

    <%-- Form tambah/ubah. Prototype: tombol Simpan belum menyimpan data --%>
    <c:if test="${not empty formMode}">
        <c:set var="kategoriTerpilih" value="${formMode == 'edit' ? editing.kategori : 'CORE'}"/>
        <form method="post" action="${ctx}/admin/features" class="panel mt-6 grid gap-10 xl:grid-cols-[1fr_320px]" data-feature-form data-validate>
          <div>
            <h2 class="text-xl font-semibold tracking-tight">
                <fmt:message key="${formMode == 'edit' ? 'admin.features.form.edit' : 'admin.features.form.new'}"/>
            </h2>
            <input type="hidden" name="action" value="save">
            <input type="hidden" name="id" value="${editing.id}">

            <div class="mt-6 grid gap-5 md:grid-cols-2">
                <div class="md:col-span-2">
                    <span id="kategori-label" class="field-label"><fmt:message key="admin.form.category"/></span>
                    <%-- Kategori tidak bisa diganti saat ubah, karena tiap kategori punya cara hitung sendiri.
                         data-select: tampilannya diganti dropdown custom oleh select.js --%>
                    <select name="kategori" class="input" data-select data-kategori-select aria-labelledby="kategori-label" ${formMode == 'edit' ? 'disabled' : ''}>
                        <c:forEach var="k" items="${kategoriList}">
                            <option value="${k}" ${k == kategoriTerpilih ? 'selected' : ''}><fmt:message key="category.${k}"/></option>
                        </c:forEach>
                    </select>
                    <c:if test="${formMode == 'edit'}"><input type="hidden" name="kategori" value="${kategoriTerpilih}"></c:if>
                </div>
                <label class="block">
                    <span class="field-label"><fmt:message key="admin.form.nameId"/></span>
                    <input type="text" name="namaId" class="input" required maxlength="100" value="<c:out value="${editing.nama.id}"/>">
                </label>
                <label class="block">
                    <span class="field-label"><fmt:message key="admin.form.nameEn"/></span>
                    <input type="text" name="namaEn" class="input" required maxlength="100" value="<c:out value="${editing.nama.en}"/>">
                </label>
                <label class="block">
                    <span class="field-label"><fmt:message key="admin.form.descId"/></span>
                    <input type="text" name="deskripsiId" class="input" maxlength="255" value="<c:out value="${editing.deskripsi.id}"/>">
                </label>
                <label class="block">
                    <span class="field-label"><fmt:message key="admin.form.descEn"/></span>
                    <input type="text" name="deskripsiEn" class="input" maxlength="255" value="<c:out value="${editing.deskripsi.en}"/>">
                </label>
                <%-- Harga diketik sebagai teks dan otomatis diformat (5.000.000) oleh admin.js.
                     Server nanti membuang titik/koma sebelum menyimpan ke database --%>
                <label class="block">
                    <span class="field-label"><fmt:message key="admin.form.price"/></span>
                    <span class="relative block">
                        <span class="pointer-events-none absolute top-1/2 left-5 -translate-y-1/2 text-sm text-muted">Rp</span>
                        <input type="text" name="hargaDasar" class="input pl-12" required inputmode="numeric" maxlength="17" autocomplete="off"
                               data-money value="<c:if test="${not empty editing}"><fmt:formatNumber value="${editing.hargaDasar}" pattern="#,##0"/></c:if>">
                    </span>
                </label>
                <label class="block" data-show-for="PLATFORM">
                    <span class="field-label"><fmt:message key="admin.form.multiplier"/></span>
                    <input type="number" name="pengali" class="input" min="1" max="9.99" step="0.01"
                           value="${kategoriTerpilih == 'PLATFORM' ? editing.pengali : ''}">
                    <span class="mt-2 block px-2 text-xs text-muted/70"><fmt:message key="admin.form.multiplierHint"/></span>
                </label>
                <label class="block" data-show-for="INTEGRATION">
                    <span class="field-label"><fmt:message key="admin.form.setup"/></span>
                    <span class="relative block">
                        <span class="pointer-events-none absolute top-1/2 left-5 -translate-y-1/2 text-sm text-muted">Rp</span>
                        <input type="text" name="biayaSetup" class="input pl-12" inputmode="numeric" maxlength="17" autocomplete="off"
                               data-money value="<c:if test="${kategoriTerpilih == 'INTEGRATION'}"><fmt:formatNumber value="${editing.biayaSetup}" pattern="#,##0"/></c:if>">
                    </span>
                </label>
                <label class="flex cursor-pointer items-start gap-3 py-2 md:col-span-2">
                    <input type="checkbox" name="aktif" class="mt-0.5 size-5 shrink-0 accent-glacier-deep" ${formMode == 'new' or editing.aktif ? 'checked' : ''}>
                    <span>
                        <span class="block text-sm font-medium"><fmt:message key="admin.form.active"/></span>
                        <span class="mt-1 block text-xs leading-relaxed text-muted"><fmt:message key="admin.form.activeHint"/></span>
                    </span>
                </label>
            </div>

            <div class="mt-8 flex gap-3">
                <button type="submit" class="btn btn-primary"><fmt:message key="admin.save"/></button>
                <a href="${ctx}/admin/features" class="btn btn-outline"><fmt:message key="admin.cancel"/></a>
            </div>
          </div>

            <%-- Preview kartu fitur seperti yang dilihat klien di halaman Hitung Estimasi. Diisi admin.js --%>
            <aside class="xl:border-l xl:border-ink/5 xl:pl-10" data-preview
                   data-lang="${sessionScope.lang}"
                   data-placeholder-name="<fmt:message key="admin.preview.name"/>"
                   data-placeholder-desc="<fmt:message key="admin.preview.desc"/>"
                   data-rule-platform="<fmt:message key="admin.preview.rule.PLATFORM"/>"
                   data-rule-database="<fmt:message key="admin.preview.rule.DATABASE"/>"
                   data-rule-core="<fmt:message key="admin.preview.rule.CORE"/>"
                   data-rule-integration="<fmt:message key="admin.preview.rule.INTEGRATION"/>">
                <p class="field-label"><fmt:message key="admin.preview.label"/></p>
                <div class="rounded-[2.5rem] bg-cream p-4">
                    <div class="flex flex-col rounded-card border border-glacier-deep/30 bg-glacier/45 p-5 shadow-soft transition duration-300" data-preview-card>
                        <span class="flex items-start justify-between gap-3">
                            <span class="font-medium" data-preview-name></span>
                            <span class="grid size-5 shrink-0 place-items-center rounded-full border-2 border-ink bg-ink" aria-hidden="true">
                                <svg class="size-3" viewBox="0 0 12 12" fill="none" stroke="#FDFCF8" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round"><path d="M2.5 6.2 5 8.5l4.5-5"/></svg>
                            </span>
                        </span>
                        <span class="mt-1 text-sm leading-relaxed text-muted" data-preview-desc></span>
                        <span class="mt-4 text-sm font-medium" data-preview-price></span>
                    </div>
                </div>
                <p class="mt-4 px-2 text-xs leading-relaxed text-muted" data-preview-rule></p>
                <p class="mt-3 hidden items-start gap-2 rounded-[1.25rem] bg-lavender px-4 py-3 text-xs leading-relaxed text-ink/80" data-preview-hidden>
                    <span class="mt-1 size-1.5 shrink-0 rounded-full bg-muted"></span><fmt:message key="admin.preview.hidden"/>
                </p>
            </aside>
        </form>
    </c:if>

    <div class="panel relative mt-6 overflow-x-auto p-0 md:p-0">
        <table class="w-full min-w-[760px] text-left text-sm">
            <thead>
                <tr class="label text-muted">
                    <th class="px-6 py-5 font-semibold"><fmt:message key="admin.col.name"/></th>
                    <th class="px-6 py-5 text-right font-semibold"><fmt:message key="admin.col.price"/></th>
                    <th class="px-6 py-5 font-semibold"><fmt:message key="admin.col.rule"/></th>
                    <th class="px-6 py-5 font-semibold"><fmt:message key="admin.col.status"/></th>
                    <th class="px-6 py-5"><span class="sr-only"><fmt:message key="admin.edit"/></span></th>
                </tr>
            </thead>
            <c:forEach var="k" items="${kategoriList}">
                <tbody>
                    <tr class="border-t border-ink/5 bg-cream">
                        <th colspan="5" class="label px-6 py-3 text-left text-glacier-deep"><fmt:message key="category.${k}"/></th>
                    </tr>
                    <c:forEach var="item" items="${items}">
                        <c:if test="${item.kategori == k}">
                            <tr class="border-t border-ink/5 ${item.aktif ? '' : 'text-muted'}">
                                <td class="px-6 py-4">
                                    <p class="font-medium"><c:out value="${item.nama.get(sessionScope.lang)}"/></p>
                                    <p class="text-xs text-muted"><c:out value="${item.deskripsi.get(sessionScope.lang)}"/></p>
                                </td>
                                <td class="px-6 py-4 text-right font-medium tabular-nums"><t:rupiah value="${item.hargaDasar}"/></td>
                                <td class="px-6 py-4 text-muted">
                                    <c:choose>
                                        <c:when test="${k == 'PLATFORM'}">&times; <fmt:formatNumber value="${item.pengali}" minFractionDigits="2" maxFractionDigits="2"/></c:when>
                                        <c:when test="${k == 'INTEGRATION'}">+ <t:rupiah value="${item.biayaSetup}"/> setup</c:when>
                                        <c:when test="${k == 'CORE'}"><fmt:message key="admin.features.multiplied"/></c:when>
                                        <c:otherwise><fmt:message key="admin.features.fixed"/></c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="px-6 py-4">
                                    <c:choose>
                                        <c:when test="${item.aktif}"><span class="badge bg-glacier text-ink"><fmt:message key="admin.features.active"/></span></c:when>
                                        <c:otherwise><span class="badge bg-ink/5 text-muted"><fmt:message key="admin.features.inactive"/></span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="px-6 py-4">
                                    <div class="flex justify-end gap-2">
                                        <a href="${ctx}/admin/features?edit=${item.id}" class="btn btn-outline btn-sm"><fmt:message key="admin.edit"/></a>
                                        <form method="post" action="${ctx}/admin/features">
                                            <input type="hidden" name="action" value="${item.aktif ? 'deactivate' : 'activate'}">
                                            <input type="hidden" name="id" value="${item.id}">
                                            <button type="submit" class="btn btn-sm ${item.aktif ? 'btn-danger' : 'btn-outline'}">
                                                <fmt:message key="${item.aktif ? 'admin.features.deactivate' : 'admin.features.activate'}"/>
                                            </button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </tbody>
            </c:forEach>
        </table>
    </div>

</t:admin-layout>
