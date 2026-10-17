<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout titleKey="home.title">

    <%-- Hero: teks di kiri, kartu estimasi kaca di kanan (di atas blob, supaya efek blur-nya terlihat) --%>
    <section class="mx-auto grid max-w-7xl items-end gap-12 px-5 pt-8 pb-16 md:px-10 md:pt-16 lg:grid-cols-[1fr_380px] lg:pb-24">
        <div data-rise>
            <p class="badge border border-ink/5 bg-white/70 text-muted backdrop-blur">
                <span class="size-1.5 rounded-full bg-glacier-deep"></span><fmt:message key="home.hero.label"/>
            </p>
            <h1 class="mt-6 text-5xl leading-[1.02] font-semibold tracking-tight md:text-7xl">
                <fmt:message key="home.hero.title"/>
                <span class="mt-2 block font-hand text-5xl leading-none font-normal tracking-normal text-glacier-deep md:text-8xl"><fmt:message key="home.hero.accent"/></span>
            </h1>
            <p class="mt-6 max-w-xl text-lg text-muted md:text-xl">
                <fmt:message key="home.hero.subtitle"/>
            </p>

            <div class="mt-10 flex flex-col gap-3 sm:flex-row">
                <a href="${ctx}/estimate" class="btn btn-primary"><fmt:message key="home.hero.cta"/></a>
                <a href="${ctx}/track" class="btn btn-outline"><fmt:message key="home.hero.track"/></a>
            </div>

            <ul class="mt-8 flex flex-wrap gap-2">
                <li class="badge bg-sage text-ink"><span class="size-1.5 rounded-full bg-glacier-deep"></span><fmt:message key="home.badge.free"/></li>
                <li class="badge bg-lavender text-ink"><span class="size-1.5 rounded-full bg-glacier-deep"></span><fmt:message key="home.badge.transparent"/></li>
            </ul>
        </div>

        <a href="${ctx}/estimate" class="glass group block animate-float p-7 md:p-8">
            <div class="flex items-start justify-between gap-4">
                <p class="label"><fmt:message key="home.card.label"/></p>
                <span class="badge bg-glacier text-ink transition group-hover:scale-105"><fmt:message key="home.card.action"/> &rarr;</span>
            </div>
            <p class="mt-10 text-sm text-muted"><fmt:message key="home.card.from"/></p>
            <%-- Prototype: angka masih tetap. Website (Rp 3.000.000) + MySQL (Rp 1.000.000) dari seed.sql --%>
            <p class="mt-1 text-4xl font-semibold tracking-tight whitespace-nowrap md:text-5xl">
                <t:rupiah value="4000000"/>
            </p>
            <p class="mt-3 text-sm text-muted"><fmt:message key="home.card.note"/></p>
        </a>
    </section>

    <%-- Portofolio --%>
    <section id="portfolio" class="mx-auto max-w-7xl scroll-mt-8 px-5 pt-8 md:px-10 md:pt-16">
        <div data-rise>
            <p class="label"><fmt:message key="home.portfolio.label"/></p>
            <h2 class="mt-3 text-4xl font-semibold tracking-tight md:text-6xl"><fmt:message key="home.portfolio.title"/></h2>
        </div>

        <ul class="mt-12 grid gap-6 md:grid-cols-2 lg:grid-cols-3">
            <c:forEach var="portfolio" items="${portfolios}">
                <li class="card flex flex-col p-3" data-rise>
                    <img src="${ctx}${portfolio.imageUrl}" alt=""
                         class="aspect-3/2 w-full rounded-[1.5rem] object-cover" loading="lazy" width="600" height="400">
                    <div class="flex flex-1 flex-col p-4 md:p-5">
                        <h3 class="text-xl font-semibold tracking-tight"><c:out value="${portfolio.judul.get(sessionScope.lang)}"/></h3>
                        <p class="mt-2 text-sm leading-relaxed text-muted"><c:out value="${portfolio.deskripsi.get(sessionScope.lang)}"/></p>
                        <ul class="mt-auto flex flex-wrap gap-2 pt-6">
                            <c:forEach var="tech" items="${portfolio.techStackList}">
                                <li class="badge bg-lavender text-ink/80"><c:out value="${tech}"/></li>
                            </c:forEach>
                        </ul>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </section>

    <%-- Cara kerja --%>
    <section class="mx-auto max-w-7xl px-5 pt-24 md:px-10 md:pt-32">
        <div class="rounded-section bg-sage px-7 py-14 md:px-16 md:py-20" data-rise>
            <p class="label"><fmt:message key="home.steps.label"/></p>
            <h2 class="mt-3 text-4xl font-semibold tracking-tight md:text-6xl"><fmt:message key="home.steps.title"/></h2>

            <ol class="mt-12 grid gap-8 md:grid-cols-3 md:gap-10">
                <c:forEach var="step" begin="1" end="3">
                    <li>
                        <span class="font-hand text-6xl leading-none text-glacier-deep">${step}.</span>
                        <p class="mt-2 max-w-xs leading-relaxed text-ink/80"><fmt:message key="home.steps.${step}"/></p>
                    </li>
                </c:forEach>
            </ol>

            <a href="${ctx}/estimate" class="btn btn-primary mt-12"><fmt:message key="home.steps.cta"/></a>
        </div>
    </section>

</t:layout>
