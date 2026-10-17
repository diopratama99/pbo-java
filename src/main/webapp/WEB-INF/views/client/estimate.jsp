<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout titleKey="estimate.title" active="estimate">

    <t:hero labelKey="estimate.hero.label" titleKey="estimate.hero.title" accentKey="estimate.hero.accent" subtitleKey="estimate.hero.subtitle"/>

    <form id="estimate-form" method="post" action="${ctx}/estimate" data-validate
          class="mx-auto grid max-w-7xl gap-12 px-5 pt-4 pb-28 md:px-10 lg:grid-cols-[1fr_380px] lg:gap-16 lg:pb-0">

        <div class="space-y-16">
            <%-- 1. Platform --%>
            <fieldset data-rise>
                <legend class="flex items-baseline gap-3">
                    <span class="font-hand text-3xl leading-none text-glacier-deep">01</span>
                    <span class="text-2xl font-semibold tracking-tight md:text-3xl"><fmt:message key="estimate.step.platform"/></span>
                </legend>
                <div class="mt-6 grid gap-3 sm:grid-cols-3">
                    <c:forEach var="feature" items="${platforms}">
                        <t:option-card feature="${feature}" type="radio" name="platform" required="true" requiredMessageKey="validation.platform"/>
                    </c:forEach>
                </div>
            </fieldset>

            <%-- 2. Database --%>
            <fieldset data-rise>
                <legend class="flex items-baseline gap-3">
                    <span class="font-hand text-3xl leading-none text-glacier-deep">02</span>
                    <span class="text-2xl font-semibold tracking-tight md:text-3xl"><fmt:message key="estimate.step.database"/></span>
                </legend>
                <p class="mt-2 text-sm text-muted"><fmt:message key="estimate.step.database.hint"/></p>
                <div class="mt-6 grid gap-3 sm:grid-cols-2">
                    <c:forEach var="feature" items="${databases}">
                        <t:option-card feature="${feature}" type="radio" name="database" required="true" requiredMessageKey="validation.database"/>
                    </c:forEach>
                </div>
            </fieldset>

            <%-- 3. Fitur utama --%>
            <fieldset data-rise>
                <legend class="flex items-baseline gap-3">
                    <span class="font-hand text-3xl leading-none text-glacier-deep">03</span>
                    <span class="text-2xl font-semibold tracking-tight md:text-3xl"><fmt:message key="estimate.step.core"/></span>
                </legend>
                <p class="mt-2 text-sm text-muted"><fmt:message key="estimate.step.core.hint"/></p>
                <div class="mt-6 grid gap-3 sm:grid-cols-2">
                    <c:forEach var="feature" items="${cores}">
                        <t:option-card feature="${feature}" type="checkbox" name="features"/>
                    </c:forEach>
                </div>
            </fieldset>

            <%-- 4. Integrasi --%>
            <fieldset data-rise>
                <legend class="flex items-baseline gap-3">
                    <span class="font-hand text-3xl leading-none text-glacier-deep">04</span>
                    <span class="text-2xl font-semibold tracking-tight md:text-3xl"><fmt:message key="estimate.step.integration"/></span>
                </legend>
                <p class="mt-2 text-sm text-muted"><fmt:message key="estimate.step.integration.hint"/></p>
                <div class="mt-6 grid gap-3 sm:grid-cols-2">
                    <c:forEach var="feature" items="${integrations}">
                        <t:option-card feature="${feature}" type="checkbox" name="features"/>
                    </c:forEach>
                </div>
            </fieldset>

            <%-- 5. Catatan --%>
            <fieldset data-rise>
                <legend class="flex items-baseline gap-3">
                    <span class="font-hand text-3xl leading-none text-glacier-deep">05</span>
                    <span class="text-2xl font-semibold tracking-tight md:text-3xl"><fmt:message key="estimate.step.notes"/></span>
                    <span class="text-sm text-muted">(<fmt:message key="common.optional"/>)</span>
                </legend>
                <textarea name="notes" rows="4" class="textarea mt-6" placeholder="<fmt:message key="estimate.notes.placeholder"/>"></textarea>
            </fieldset>

            <%-- 6. Data diri --%>
            <fieldset data-rise>
                <legend class="flex items-baseline gap-3">
                    <span class="font-hand text-3xl leading-none text-glacier-deep">06</span>
                    <span class="text-2xl font-semibold tracking-tight md:text-3xl"><fmt:message key="estimate.step.contact"/></span>
                </legend>
                <p class="mt-2 text-sm text-muted"><fmt:message key="estimate.contact.hint"/></p>
                <div class="mt-6 grid gap-5 sm:grid-cols-2">
                    <label class="block">
                        <span class="field-label"><fmt:message key="estimate.form.name"/></span>
                        <input type="text" name="nama" class="input" required maxlength="100" autocomplete="name">
                    </label>
                    <label class="block">
                        <span class="field-label"><fmt:message key="estimate.form.company"/> (<fmt:message key="common.optional"/>)</span>
                        <input type="text" name="instansi" class="input" maxlength="100" autocomplete="organization">
                    </label>
                    <label class="block">
                        <span class="field-label"><fmt:message key="estimate.form.whatsapp"/></span>
                        <input type="tel" name="noWa" class="input" required maxlength="20" inputmode="tel" autocomplete="tel" placeholder="08xxxxxxxxxx"
                               pattern="[0-9+ ]{9,20}" data-msg-pattern="<fmt:message key="validation.whatsapp"/>">
                    </label>
                    <label class="block">
                        <span class="field-label"><fmt:message key="estimate.form.email"/></span>
                        <input type="email" name="email" class="input" required maxlength="100" autocomplete="email">
                    </label>
                </div>
            </fieldset>
        </div>

        <%-- Ringkasan harga. Di laptop menempel di sisi kanan saat scroll.
             Tingginya dibatasi setinggi layar: total dan tombol kirim selalu terlihat,
             daftar fitur yang panjang di-scroll di dalam kartu --%>
        <aside id="summary" class="lg:sticky lg:top-6 lg:self-start">
            <div class="glass flex flex-col p-7 md:p-8 lg:max-h-[calc(100dvh-3rem)]"
                 data-summary>
                <p class="label"><fmt:message key="estimate.summary.label"/></p>
                <p class="mt-4 text-4xl font-semibold tracking-tight whitespace-nowrap" data-total>Rp 0</p>
                <p class="mt-2 text-sm text-muted"><span data-count>0</span> <fmt:message key="estimate.summary.count"/></p>

                <div class="mt-6 border-t border-ink/10 pt-5 lg:-mr-3 lg:min-h-0 lg:overflow-y-auto lg:overscroll-contain lg:pr-3 lg:[scrollbar-width:thin]">
                    <p class="text-sm text-muted" data-empty><fmt:message key="estimate.summary.empty"/></p>
                    <ul class="space-y-2.5 text-sm" data-lines></ul>
                </div>

                <div>
                    <dl class="mt-5 hidden space-y-2 border-t border-ink/10 pt-5 text-sm" data-discount-rows>
                        <div class="flex justify-between gap-4 text-muted">
                            <dt><fmt:message key="common.subtotal"/></dt><dd data-subtotal></dd>
                        </div>
                        <div class="flex justify-between gap-4 font-medium text-glacier-deep">
                            <dt><fmt:message key="common.discount"/></dt><dd data-discount></dd>
                        </div>
                    </dl>
                </div>

                <p class="mt-6 badge self-start bg-lavender text-ink/80" data-bundle-hint>
                    <span class="size-1.5 rounded-full bg-glacier-deep"></span><fmt:message key="estimate.summary.bundle"/>
                </p>
                <p class="mt-6 hidden badge self-start bg-glacier text-ink" data-bundle-on>
                    <span class="size-1.5 rounded-full bg-glacier-deep"></span><fmt:message key="estimate.summary.bundleOn"/>
                </p>

                <button type="submit" class="btn btn-primary mt-8 w-full"><fmt:message key="estimate.form.submit"/></button>
                <p class="mt-4 text-xs leading-relaxed text-muted"><fmt:message key="estimate.summary.note"/></p>
            </div>
        </aside>

        <%-- Bar total di HP, supaya harga selalu terlihat saat scroll --%>
        <a href="#summary"
           class="fixed inset-x-3 bottom-3 z-30 flex items-center justify-between gap-4 rounded-full border border-white bg-white/80 py-2.5 pr-2.5 pl-6 shadow-soft backdrop-blur-xl lg:hidden">
            <span class="text-lg font-semibold tracking-tight" data-total>Rp 0</span>
            <span class="btn btn-primary btn-sm"><fmt:message key="estimate.mobile.see"/></span>
        </a>
    </form>

    <script src="${ctx}/assets/js/estimate.js" defer></script>
</t:layout>
