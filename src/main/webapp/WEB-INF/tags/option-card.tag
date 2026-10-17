<%@ tag pageEncoding="UTF-8" description="Kartu pilihan fitur di kalkulator (radio atau checkbox). Berubah warna glacier saat dipilih." %>
<%@ attribute name="feature" required="true" type="com.pbojava.model.feature.Feature" %>
<%@ attribute name="type" required="true" description="radio atau checkbox" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="required" required="false" type="java.lang.Boolean" %>
<%@ attribute name="requiredMessageKey" required="false" description="Key pesan error kalau belum ada yang dipilih" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<label class="group relative flex cursor-pointer flex-col rounded-card border border-ink/5 bg-white p-5 shadow-soft transition duration-300 ease-soft hover:border-ink/15 active:scale-[0.98] has-checked:border-glacier-deep/30 has-checked:bg-glacier/45 has-focus-visible:ring-4 has-focus-visible:ring-glacier/50 has-aria-invalid:border-danger/30">
    <%-- Data harga dibaca estimate.js untuk menghitung preview --%>
    <input type="${type}" name="${name}" value="${feature.id}" class="sr-only" ${required ? 'required' : ''}
           <c:if test="${not empty requiredMessageKey}">data-msg-required="<fmt:message key="${requiredMessageKey}"/>"</c:if>
           data-kategori="${feature.kategori}"
           data-harga="${feature.hargaDasar}"
           data-nama="<c:out value="${feature.nama.get(sessionScope.lang)}"/>"
           <c:if test="${feature.kategori == 'PLATFORM'}">data-pengali="${feature.pengali}"</c:if>
           <c:if test="${feature.kategori == 'INTEGRATION'}">data-setup="${feature.biayaSetup}"</c:if>>
    <span class="flex items-start justify-between gap-3">
        <span class="font-medium"><c:out value="${feature.nama.get(sessionScope.lang)}"/></span>
        <span class="grid size-5 shrink-0 place-items-center rounded-full border-2 border-ink/15 transition group-has-checked:border-ink group-has-checked:bg-ink" aria-hidden="true">
            <svg class="size-3 opacity-0 transition group-has-checked:opacity-100" viewBox="0 0 12 12" fill="none" stroke="#FDFCF8" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round"><path d="M2.5 6.2 5 8.5l4.5-5"/></svg>
        </span>
    </span>
    <span class="mt-1 text-sm leading-relaxed text-muted"><c:out value="${feature.deskripsi.get(sessionScope.lang)}"/></span>
    <span class="mt-4 text-sm font-medium" data-price>
        <c:choose>
            <c:when test="${feature.kategori == 'INTEGRATION'}"><t:rupiah value="${feature.hargaDasar + feature.biayaSetup}"/></c:when>
            <c:otherwise><t:rupiah value="${feature.hargaDasar}"/></c:otherwise>
        </c:choose>
    </span>
</label>
