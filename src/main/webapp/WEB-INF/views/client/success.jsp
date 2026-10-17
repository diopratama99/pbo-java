<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout titleKey="success.title">

    <t:hero labelKey="success.hero.label" titleKey="success.hero.title" accentKey="success.hero.accent" subtitleKey="success.hero.subtitle"/>

    <div class="mx-auto max-w-7xl px-5 md:px-10">
        <div class="glass max-w-xl animate-float p-7 md:p-10">
            <div class="flex items-start justify-between gap-4">
                <p class="label"><fmt:message key="success.card.label"/></p>
                <button type="button" class="badge cursor-pointer bg-glacier text-ink transition hover:scale-105 active:scale-95"
                        data-copy="#ticket-code"
                        data-copied-text="<fmt:message key="success.copied"/>">
                    <fmt:message key="success.copy"/>
                </button>
            </div>
            <p id="ticket-code" class="mt-6 text-5xl font-semibold tracking-tight break-all md:text-7xl"><c:out value="${ticket}"/></p>
        </div>

        <div class="mt-10 flex flex-col gap-3 sm:flex-row">
            <a href="${ctx}/track" class="btn btn-primary"><fmt:message key="success.track"/></a>
            <a href="${ctx}/" class="btn btn-outline"><fmt:message key="common.home"/></a>
        </div>
    </div>

    <script src="${ctx}/assets/js/copy.js" defer></script>
</t:layout>
