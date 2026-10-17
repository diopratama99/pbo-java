<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout titleKey="team.title" active="team">

    <t:hero labelKey="team.hero.label" titleKey="team.hero.title" accentKey="team.hero.accent" subtitleKey="team.hero.subtitle"/>

    <section class="mx-auto max-w-7xl px-5 pt-4 md:px-10">
        <ul class="grid gap-16 md:grid-cols-3 md:gap-10">
            <c:forEach var="member" items="${members}" varStatus="loop">
                <%-- Warna dan bentuk tiap kartu dibuat beda supaya tidak terlihat seperti tumpukan kotak --%>
                <c:choose>
                    <c:when test="${loop.index % 3 == 0}"><c:set var="shape" value="bg-sage rounded-tl-blob rounded-br-blob rounded-tr-card rounded-bl-card"/></c:when>
                    <c:when test="${loop.index % 3 == 1}"><c:set var="shape" value="bg-glacier/70 rounded-t-blob rounded-b-card"/></c:when>
                    <c:otherwise><c:set var="shape" value="bg-lavender rounded-tr-blob rounded-bl-blob rounded-tl-card rounded-br-card"/></c:otherwise>
                </c:choose>

                <li class="${loop.index % 3 == 1 ? 'md:mt-16' : ''}" data-rise>
                    <div class="relative aspect-4/5 overflow-hidden shadow-soft ${shape}">
                        <%-- Foto PNG tanpa background. Kalau file belum ada, tampil siluet placeholder --%>
                        <img src="${ctx}${member.foto}" alt="<c:out value="${member.nama}"/>"
                             onerror="this.onerror=null;this.src='${ctx}/assets/img/team/placeholder.svg'"
                             class="absolute inset-x-0 bottom-0 mx-auto h-[92%] w-auto max-w-none object-contain object-bottom"
                             loading="lazy">
                    </div>

                    <p class="mt-7 badge border border-ink/5 bg-white text-ink/80 shadow-soft"><c:out value="${member.posisi.get(sessionScope.lang)}"/></p>
                    <h2 class="mt-3 text-4xl font-semibold tracking-tight md:text-5xl"><c:out value="${member.nama}"/></h2>

                    <dl class="mt-6 space-y-6 text-sm">
                        <div>
                            <dt class="label"><fmt:message key="team.education"/></dt>
                            <dd class="mt-1.5 leading-relaxed text-ink/80"><c:out value="${member.pendidikan.get(sessionScope.lang)}"/></dd>
                        </div>
                        <div>
                            <dt class="label"><fmt:message key="team.experience"/></dt>
                            <dd>
                                <ul class="mt-2 space-y-2.5">
                                    <c:forEach var="item" items="${member.pengalaman}">
                                        <li class="flex gap-3 leading-relaxed text-ink/80">
                                            <span class="mt-2 size-1.5 shrink-0 rounded-full bg-glacier-deep"></span>
                                            <span><c:out value="${item.get(sessionScope.lang)}"/></span>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </dd>
                        </div>
                    </dl>
                </li>
            </c:forEach>
        </ul>
    </section>

</t:layout>
