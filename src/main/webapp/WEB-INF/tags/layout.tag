<%@ tag pageEncoding="UTF-8" description="Layout halaman klien: head, navbar kuning, footer" %>
<%@ attribute name="titleKey" required="true" description="Key judul halaman di file terjemahan" %>
<%@ attribute name="active" required="false" description="Menu yang aktif: portfolio, estimate, atau track" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><fmt:message key="${titleKey}"/> | <fmt:message key="app.name"/></title>
    <link rel="preload" href="${ctx}/assets/fonts/inter/InterVariable-latin.woff2" as="font" type="font/woff2" crossorigin>
    <link rel="stylesheet" href="${ctx}/assets/css/app.css">
    <script>
        if (!window.matchMedia('(prefers-reduced-motion: reduce)').matches) {
            document.documentElement.classList.add('motion');
        }
    </script>
    <script src="${ctx}/assets/js/motion.js" defer></script>
</head>
<body class="min-h-screen">

<header class="bg-shout text-black">
    <nav class="mx-auto flex max-w-7xl items-center justify-between gap-4 px-5 py-5 md:px-10 md:py-7">
        <a href="${ctx}/" class="text-2xl font-bold tracking-tight md:text-3xl" aria-label="<fmt:message key="nav.home"/>">
            <fmt:message key="app.name"/>
        </a>

        <div class="flex items-center gap-2 md:gap-8">
            <ul class="hidden items-center gap-8 text-sm font-medium md:flex">
                <li><a href="${ctx}/#portfolio" class="${active == 'portfolio' ? 'underline underline-offset-8 decoration-2' : 'opacity-70 hover:opacity-100'} transition"><fmt:message key="nav.portfolio"/></a></li>
                <li><a href="${ctx}/estimate" class="${active == 'estimate' ? 'underline underline-offset-8 decoration-2' : 'opacity-70 hover:opacity-100'} transition"><fmt:message key="nav.estimate"/></a></li>
                <li><a href="${ctx}/track" class="${active == 'track' ? 'underline underline-offset-8 decoration-2' : 'opacity-70 hover:opacity-100'} transition"><fmt:message key="nav.track"/></a></li>
            </ul>

            <%-- Tombol ID / EN. Link-nya disiapkan oleh LanguageFilter --%>
            <div class="flex rounded-full border-2 border-black/20 p-1 text-xs font-semibold" role="group" aria-label="<fmt:message key="nav.lang"/>">
                <a href="${langUrlId}" class="rounded-full px-3 py-1.5 transition ${sessionScope.lang == 'id' ? 'bg-black text-white' : 'hover:bg-black/10'}" hreflang="id" lang="id">ID</a>
                <a href="${langUrlEn}" class="rounded-full px-3 py-1.5 transition ${sessionScope.lang == 'en' ? 'bg-black text-white' : 'hover:bg-black/10'}" hreflang="en" lang="en">EN</a>
            </div>

            <%-- Menu HP: pakai <details>, jadi tidak butuh JavaScript --%>
            <details class="group relative md:hidden">
                <summary class="btn btn-dark cursor-pointer list-none px-5 py-2.5 [&::-webkit-details-marker]:hidden">
                    <fmt:message key="nav.menu"/>
                </summary>
                <ul class="absolute right-0 z-20 mt-3 w-56 rounded-[28px] bg-black p-2 text-sm font-medium text-white shadow-2xl">
                    <li><a href="${ctx}/#portfolio" class="block rounded-full px-4 py-3 hover:bg-white/10"><fmt:message key="nav.portfolio"/></a></li>
                    <li><a href="${ctx}/estimate" class="block rounded-full px-4 py-3 hover:bg-white/10"><fmt:message key="nav.estimate"/></a></li>
                    <li><a href="${ctx}/track" class="block rounded-full px-4 py-3 hover:bg-white/10"><fmt:message key="nav.track"/></a></li>
                </ul>
            </details>
        </div>
    </nav>
</header>

<main>
    <jsp:doBody/>
</main>

<footer class="overflow-hidden pt-20 pb-10">
    <p class="label mx-auto max-w-7xl px-5 text-white/40 md:px-10"><fmt:message key="footer.stack"/></p>

    <%-- Ticker tech stack. Daftarnya ditulis 2 kali supaya animasinya bisa berputar tanpa putus --%>
    <div class="mt-6 flex w-max animate-ticker *:pr-16 text-2xl font-bold tracking-tight text-white/30 md:text-4xl" aria-hidden="true">
        <c:forEach begin="1" end="2">
            <span>Java</span><span>Jakarta Servlet</span><span>JSP</span><span>MySQL</span><span>Tailwind CSS</span><span>JavaScript</span>
            <span>Java</span><span>Jakarta Servlet</span><span>JSP</span><span>MySQL</span><span>Tailwind CSS</span><span>JavaScript</span>
        </c:forEach>
    </div>
    <p class="sr-only">Java, Jakarta Servlet, JSP, MySQL, Tailwind CSS, JavaScript</p>

    <div class="mx-auto mt-16 flex max-w-7xl flex-col gap-2 border-t border-white/10 px-5 pt-8 text-xs text-white/40 md:flex-row md:items-center md:justify-between md:px-10">
        <span class="text-base font-bold tracking-tight text-white"><fmt:message key="app.name"/></span>
        <span>&copy; 2026 <fmt:message key="footer.copyright"/></span>
    </div>
</footer>

</body>
</html>
