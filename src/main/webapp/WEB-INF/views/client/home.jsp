<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout titleKey="home.title">

    <%-- Hero kuning. Lanjutan dari navbar di layout, jadi warnanya menyatu --%>
    <section class="rounded-br-[64px] rounded-bl-[40px] bg-shout pb-36 text-black md:rounded-br-liquid md:pb-48">
        <div class="mx-auto max-w-7xl px-5 pt-10 md:px-10 md:pt-20" data-rise>
            <p class="label opacity-60"><fmt:message key="home.hero.label"/></p>
            <h1 class="mt-5 max-w-4xl text-5xl leading-[0.95] font-bold tracking-tight sm:text-6xl md:text-8xl">
                <fmt:message key="home.hero.title"/>
            </h1>
            <p class="mt-6 max-w-xl text-lg font-medium opacity-80 md:text-xl">
                <fmt:message key="home.hero.subtitle"/>
            </p>

            <div class="mt-10 flex flex-col gap-3 sm:flex-row">
                <a href="${ctx}/estimate" class="btn btn-dark"><fmt:message key="home.hero.cta"/></a>
                <a href="${ctx}/track" class="btn btn-outline"><fmt:message key="home.hero.track"/></a>
            </div>

            <ul class="mt-10 flex flex-wrap gap-2">
                <li class="badge bg-black text-white">
                    <span class="size-1.5 rounded-full bg-shout"></span><fmt:message key="home.badge.free"/>
                </li>
                <li class="badge bg-black text-white">
                    <span class="size-1.5 rounded-full bg-shout"></span><fmt:message key="home.badge.transparent"/>
                </li>
            </ul>
        </div>
    </section>

    <%-- Glass card di perbatasan kuning dan hitam.
         Pakai kaca gelap (bg-black/50) supaya teks putih tetap terbaca di atas kuning maupun hitam. --%>
    <div class="relative z-10 mx-auto -mt-24 max-w-7xl px-5 md:-mt-32 md:px-10">
        <a href="${ctx}/estimate"
           class="group ml-auto block max-w-sm animate-float rounded-glass border border-white/20 bg-black/50 p-7 shadow-2xl backdrop-blur-2xl transition duration-500 ease-liquid hover:bg-black/60 md:p-8">
            <div class="flex items-start justify-between gap-4">
                <p class="label text-white/60"><fmt:message key="home.card.label"/></p>
                <span class="badge bg-shout text-black transition group-hover:scale-105"><fmt:message key="home.card.action"/> &rarr;</span>
            </div>
            <p class="mt-8 text-sm text-white/60"><fmt:message key="home.card.from"/></p>
            <%-- Prototype: angka masih tetap. Website (Rp 3.000.000) + MySQL (Rp 1.000.000) dari seed.sql --%>
            <p class="mt-1 text-4xl font-bold tracking-tight md:text-5xl">
                Rp <fmt:formatNumber value="4000000" pattern="#,##0"/>
            </p>
            <p class="mt-3 text-sm text-white/60"><fmt:message key="home.card.note"/></p>
        </a>
    </div>

    <%-- Portofolio di area hitam --%>
    <section id="portfolio" class="mx-auto max-w-7xl scroll-mt-8 px-5 pt-20 md:px-10 md:pt-28">
        <div data-rise>
            <p class="label text-shout"><fmt:message key="home.portfolio.label"/></p>
            <h2 class="mt-4 text-4xl font-bold tracking-tight md:text-6xl"><fmt:message key="home.portfolio.title"/></h2>
        </div>

        <ul class="mt-12 grid gap-6 md:grid-cols-2 lg:grid-cols-3">
            <c:forEach var="portfolio" items="${portfolios}">
                <li class="glass flex flex-col p-3" data-rise>
                    <img src="${ctx}${portfolio.imageUrl}" alt=""
                         class="aspect-3/2 w-full rounded-3xl object-cover" loading="lazy" width="600" height="400">
                    <div class="flex flex-1 flex-col p-4 md:p-5">
                        <h3 class="text-xl font-bold tracking-tight"><c:out value="${portfolio.judul.get(sessionScope.lang)}"/></h3>
                        <p class="mt-2 text-sm leading-relaxed text-white/60"><c:out value="${portfolio.deskripsi.get(sessionScope.lang)}"/></p>
                        <ul class="mt-auto flex flex-wrap gap-2 pt-6">
                            <c:forEach var="tech" items="${portfolio.techStackList}">
                                <li class="badge bg-deep text-white/80"><c:out value="${tech}"/></li>
                            </c:forEach>
                        </ul>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </section>

    <%-- Kuning muncul lagi di kedalaman berbeda, sudut asimetris kebalikan dari hero --%>
    <section class="mt-24 ml-4 rounded-tl-[64px] rounded-bl-[40px] bg-shout text-black md:mt-32 md:ml-24 md:rounded-tl-liquid">
        <div class="max-w-6xl px-7 py-16 md:px-16 md:py-24" data-rise>
            <p class="label opacity-60"><fmt:message key="home.steps.label"/></p>
            <h2 class="mt-4 text-4xl font-bold tracking-tight md:text-6xl"><fmt:message key="home.steps.title"/></h2>

            <ol class="mt-12 grid gap-8 md:grid-cols-3 md:gap-10">
                <c:forEach var="step" begin="1" end="3">
                    <li>
                        <span class="text-5xl font-bold tracking-tight opacity-20 md:text-6xl">0${step}</span>
                        <p class="mt-3 max-w-xs text-sm leading-relaxed font-medium"><fmt:message key="home.steps.${step}"/></p>
                    </li>
                </c:forEach>
            </ol>

            <a href="${ctx}/estimate" class="btn btn-dark mt-12"><fmt:message key="home.steps.cta"/></a>
        </div>
    </section>

</t:layout>
