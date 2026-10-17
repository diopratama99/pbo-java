<%@ tag pageEncoding="UTF-8" description="Layout halaman klien: head, navbar, blob background, footer" %>
<%@ attribute name="titleKey" required="true" description="Key judul halaman di file terjemahan" %>
<%@ attribute name="active" required="false" description="Menu yang aktif: team, estimate, atau track" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><fmt:message key="${titleKey}"/> | <fmt:message key="app.name"/></title>
    <link rel="preload" href="${ctx}/assets/fonts/outfit/OutfitVariable-latin.woff2" as="font" type="font/woff2" crossorigin>
    <link rel="stylesheet" href="${ctx}/assets/css/app.css">
    <script>
        if (!window.matchMedia('(prefers-reduced-motion: reduce)').matches) {
            document.documentElement.classList.add('motion');
        }
    </script>
    <script src="${ctx}/assets/js/motion.js" defer></script>
    <script src="${ctx}/assets/js/validate.js" defer></script>
    <t:validation-messages/>
</head>
<body class="grain min-h-screen">

<div class="relative isolate overflow-x-clip">
    <%-- Blob blur di background. Warnanya pastel dan bergerak pelan --%>
    <div class="absolute inset-x-0 top-0 -z-10 h-[56rem]" aria-hidden="true">
        <div class="blob -top-24 -left-24 size-[26rem] bg-sage"></div>
        <div class="blob top-24 right-[-6rem] size-[30rem] bg-lavender [animation-delay:-2s]"></div>
        <div class="blob top-[26rem] left-1/3 size-[22rem] bg-glacier/60 [animation-delay:-4s]"></div>
    </div>

    <header>
        <nav class="mx-auto flex max-w-7xl items-center justify-between gap-4 px-5 py-5 md:px-10 md:py-7">
            <a href="${ctx}/" class="text-2xl font-semibold tracking-tight md:text-3xl" aria-label="<fmt:message key="nav.home"/>">
                <fmt:message key="app.name"/>
            </a>

            <div class="flex items-center gap-2 lg:gap-8">
                <ul class="hidden items-center gap-8 text-sm lg:flex">
                    <li><a href="${ctx}/#portfolio" class="text-muted transition hover:text-ink"><fmt:message key="nav.portfolio"/></a></li>
                    <li><a href="${ctx}/team" class="${active == 'team' ? 'font-medium text-ink' : 'text-muted hover:text-ink'} transition"><fmt:message key="nav.team"/></a></li>
                    <li><a href="${ctx}/estimate" class="${active == 'estimate' ? 'font-medium text-ink' : 'text-muted hover:text-ink'} transition"><fmt:message key="nav.estimate"/></a></li>
                    <li><a href="${ctx}/track" class="${active == 'track' ? 'font-medium text-ink' : 'text-muted hover:text-ink'} transition"><fmt:message key="nav.track"/></a></li>
                </ul>

                <%-- Tombol ID / EN. Link-nya disiapkan oleh LanguageFilter --%>
                <div class="flex rounded-full border border-ink/10 bg-white/60 p-1 text-xs font-medium backdrop-blur" role="group" aria-label="<fmt:message key="nav.lang"/>">
                    <a href="${langUrlId}" class="rounded-full px-3 py-1.5 transition ${sessionScope.lang == 'id' ? 'bg-ink text-cream' : 'text-muted hover:text-ink'}" hreflang="id" lang="id">ID</a>
                    <a href="${langUrlEn}" class="rounded-full px-3 py-1.5 transition ${sessionScope.lang == 'en' ? 'bg-ink text-cream' : 'text-muted hover:text-ink'}" hreflang="en" lang="en">EN</a>
                </div>

                <%-- Menu HP dan tablet: pakai <details>, jadi tidak butuh JavaScript --%>
                <details class="relative lg:hidden">
                    <summary class="btn btn-primary cursor-pointer list-none px-5 py-2.5 [&::-webkit-details-marker]:hidden">
                        <fmt:message key="nav.menu"/>
                    </summary>
                    <ul class="card absolute right-0 z-20 mt-3 w-56 p-2 text-sm">
                        <li><a href="${ctx}/#portfolio" class="block rounded-full px-4 py-3 hover:bg-sage"><fmt:message key="nav.portfolio"/></a></li>
                        <li><a href="${ctx}/team" class="block rounded-full px-4 py-3 hover:bg-sage"><fmt:message key="nav.team"/></a></li>
                        <li><a href="${ctx}/estimate" class="block rounded-full px-4 py-3 hover:bg-sage"><fmt:message key="nav.estimate"/></a></li>
                        <li><a href="${ctx}/track" class="block rounded-full px-4 py-3 hover:bg-sage"><fmt:message key="nav.track"/></a></li>
                    </ul>
                </details>
            </div>
        </nav>
    </header>

    <main>
        <jsp:doBody/>
    </main>

    <footer class="pt-24 pb-10">
        <div class="mx-auto flex max-w-7xl flex-col gap-2 border-t border-ink/10 px-5 pt-8 text-xs text-muted md:flex-row md:items-center md:justify-between md:px-10">
            <span class="text-base font-semibold tracking-tight text-ink"><fmt:message key="app.name"/></span>
            <span>&copy; 2026 <fmt:message key="footer.copyright"/></span>
        </div>
    </footer>
</div>

</body>
</html>
