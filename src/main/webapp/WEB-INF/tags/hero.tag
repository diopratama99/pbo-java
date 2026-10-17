<%@ tag pageEncoding="UTF-8" description="Hero halaman klien. Isi tambahan (misalnya form) ditulis di dalam tag ini." %>
<%@ attribute name="labelKey" required="true" %>
<%@ attribute name="titleKey" required="true" %>
<%@ attribute name="accentKey" required="false" description="Teks tulisan tangan (Reenie Beanie) di bawah judul" %>
<%@ attribute name="subtitleKey" required="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<section class="mx-auto max-w-7xl px-5 pt-8 pb-12 md:px-10 md:pt-16 md:pb-16" data-rise>
    <p class="badge border border-ink/5 bg-white/70 text-muted backdrop-blur">
        <span class="size-1.5 rounded-full bg-glacier-deep"></span><fmt:message key="${labelKey}"/>
    </p>
    <h1 class="mt-6 max-w-4xl text-5xl leading-[1.02] font-semibold tracking-tight md:text-8xl">
        <fmt:message key="${titleKey}"/>
        <c:if test="${not empty accentKey}">
            <span class="mt-1 block font-hand text-6xl leading-none font-normal tracking-normal text-glacier-deep md:text-8xl"><fmt:message key="${accentKey}"/></span>
        </c:if>
    </h1>
    <c:if test="${not empty subtitleKey}">
        <p class="mt-6 max-w-xl text-lg text-muted md:text-xl"><fmt:message key="${subtitleKey}"/></p>
    </c:if>
    <jsp:doBody/>
</section>
