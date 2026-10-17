<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:admin-layout titleKey="admin.portfolios.title" active="portfolios">

    <div class="flex flex-wrap items-center justify-between gap-4">
        <h1 class="text-3xl font-semibold tracking-tight md:text-4xl"><fmt:message key="admin.portfolios.title"/></h1>
        <c:if test="${empty formMode}">
            <a href="${ctx}/admin/portfolios?new" class="btn btn-primary"><fmt:message key="admin.portfolios.add"/></a>
        </c:if>
    </div>

    <%-- Form tambah/ubah. Prototype: tombol Simpan belum menyimpan data --%>
    <c:if test="${not empty formMode}">
        <form method="post" action="${ctx}/admin/portfolios" class="panel mt-6" data-validate>
            <h2 class="text-xl font-semibold tracking-tight">
                <fmt:message key="${formMode == 'edit' ? 'admin.portfolios.form.edit' : 'admin.portfolios.form.new'}"/>
            </h2>
            <input type="hidden" name="action" value="save">
            <input type="hidden" name="id" value="${editing.id}">

            <div class="mt-6 grid gap-5 md:grid-cols-2">
                <label class="block">
                    <span class="field-label"><fmt:message key="admin.form.titleId"/></span>
                    <input type="text" name="judulId" class="input" required maxlength="150" value="<c:out value="${editing.judul.id}"/>">
                </label>
                <label class="block">
                    <span class="field-label"><fmt:message key="admin.form.titleEn"/></span>
                    <input type="text" name="judulEn" class="input" required maxlength="150" value="<c:out value="${editing.judul.en}"/>">
                </label>
                <label class="block">
                    <span class="field-label"><fmt:message key="admin.form.descId"/></span>
                    <textarea name="deskripsiId" rows="3" class="textarea"><c:out value="${editing.deskripsi.id}"/></textarea>
                </label>
                <label class="block">
                    <span class="field-label"><fmt:message key="admin.form.descEn"/></span>
                    <textarea name="deskripsiEn" rows="3" class="textarea"><c:out value="${editing.deskripsi.en}"/></textarea>
                </label>
                <label class="block">
                    <span class="field-label"><fmt:message key="admin.form.stack"/></span>
                    <input type="text" name="techStack" class="input" maxlength="255" placeholder="Java, MySQL" value="<c:out value="${editing.techStack}"/>">
                    <span class="mt-2 block px-2 text-xs text-muted/70"><fmt:message key="admin.form.stackHint"/></span>
                </label>
                <label class="block">
                    <span class="field-label"><fmt:message key="admin.form.image"/></span>
                    <input type="text" name="imageUrl" class="input" maxlength="255" placeholder="/assets/img/portfolio/nama-file.svg" value="<c:out value="${editing.imageUrl}"/>">
                </label>
            </div>

            <div class="mt-8 flex gap-3">
                <button type="submit" class="btn btn-primary"><fmt:message key="admin.save"/></button>
                <a href="${ctx}/admin/portfolios" class="btn btn-outline"><fmt:message key="admin.cancel"/></a>
            </div>
        </form>
    </c:if>

    <ul class="mt-6 grid gap-4 md:grid-cols-2 2xl:grid-cols-3">
        <c:forEach var="item" items="${items}">
            <li class="panel flex flex-col p-3 md:p-3">
                <img src="${ctx}${item.imageUrl}" alt="" class="aspect-3/2 w-full rounded-3xl object-cover" loading="lazy" width="600" height="400">
                <div class="flex flex-1 flex-col p-3">
                    <h2 class="font-semibold"><c:out value="${item.judul.get(sessionScope.lang)}"/></h2>
                    <p class="mt-1 text-xs text-muted"><c:out value="${item.techStack}"/></p>
                    <div class="mt-5 flex gap-2">
                        <a href="${ctx}/admin/portfolios?edit=${item.id}" class="btn btn-outline btn-sm"><fmt:message key="admin.edit"/></a>
                        <form method="post" action="${ctx}/admin/portfolios">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="${item.id}">
                            <button type="submit" class="btn btn-danger btn-sm"><fmt:message key="admin.delete"/></button>
                        </form>
                    </div>
                </div>
            </li>
        </c:forEach>
    </ul>

</t:admin-layout>
