<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout titleKey="track.title" active="track">

    <t:hero labelKey="track.hero.label" titleKey="track.hero.title" accentKey="track.hero.accent">
        <%-- Form cek status langsung di hero, karena ini satu-satunya aksi di halaman ini --%>
        <form method="post" action="${ctx}/track" data-validate class="mt-10 grid max-w-3xl items-start gap-3 md:grid-cols-[1fr_1fr_auto]">
            <label class="block">
                <span class="sr-only"><fmt:message key="track.form.ticket"/></span>
                <input type="text" name="ticket" required maxlength="12" value="<c:out value="${ticket}"/>"
                       placeholder="<fmt:message key="track.form.ticket"/>" autocapitalize="characters" spellcheck="false"
                       class="input uppercase shadow-soft placeholder:normal-case">
            </label>
            <label class="block">
                <span class="sr-only"><fmt:message key="track.form.email"/></span>
                <input type="email" name="email" required maxlength="100" value="<c:out value="${email}"/>"
                       placeholder="<fmt:message key="track.form.email"/>" autocomplete="email"
                       class="input shadow-soft">
            </label>
            <button type="submit" class="btn btn-primary"><fmt:message key="track.form.submit"/></button>
        </form>
    </t:hero>

    <c:if test="${searched}">
        <div class="mx-auto max-w-7xl px-5 md:px-10">
            <div class="glass max-w-2xl p-7 md:p-10">
                <c:choose>
                    <c:when test="${not empty result}">
                        <div class="flex flex-wrap items-start justify-between gap-4">
                            <div>
                                <p class="label"><fmt:message key="track.result.label"/></p>
                                <p class="mt-2 text-3xl font-semibold tracking-tight md:text-4xl"><c:out value="${result.kodeTiket}"/></p>
                            </div>
                            <t:status-badge status="${result.status}"/>
                        </div>

                        <p class="mt-6 text-lg leading-relaxed"><fmt:message key="track.msg.${result.status}"/></p>

                        <c:if test="${result.status == 'REJECTED'}">
                            <div class="mt-5 rounded-[1.5rem] bg-lavender p-5">
                                <p class="label"><fmt:message key="track.reason"/></p>
                                <p class="mt-2 text-sm leading-relaxed text-ink/80"><c:out value="${result.alasanDitolak}"/></p>
                            </div>
                        </c:if>

                        <dl class="mt-8 grid grid-cols-2 gap-6 border-t border-ink/10 pt-6 text-sm">
                            <div>
                                <dt class="label"><fmt:message key="track.result.date"/></dt>
                                <dd class="mt-1 font-medium">${result.formatTanggal(sessionScope.lang)}</dd>
                            </div>
                            <div>
                                <dt class="label"><fmt:message key="track.result.total"/></dt>
                                <dd class="mt-1 font-medium"><t:rupiah value="${result.totalEstimasi}"/></dd>
                            </div>
                        </dl>

                        <c:if test="${result.status == 'APPROVED'}">
                            <a href="<c:out value="${result.wagLink}"/>" target="_blank" rel="noopener noreferrer"
                               class="btn btn-primary mt-8 w-full sm:w-auto"><fmt:message key="track.joinGroup"/> &rarr;</a>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <p class="text-2xl font-semibold tracking-tight md:text-3xl"><fmt:message key="track.notFound.title"/></p>
                        <p class="mt-3 text-sm leading-relaxed text-muted"><fmt:message key="track.notFound.body"/></p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </c:if>

</t:layout>
