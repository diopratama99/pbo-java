<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:admin-layout titleKey="admin.login.title" bare="true">

    <div class="panel md:p-10">
        <p class="text-3xl font-semibold tracking-tight"><fmt:message key="app.name"/></p>
        <h1 class="mt-8 text-2xl font-semibold tracking-tight"><fmt:message key="admin.login.title"/></h1>
        <p class="mt-1 text-sm text-muted"><fmt:message key="admin.login.subtitle"/></p>

        <c:if test="${loginFailed}">
            <p class="mt-6 flex items-center gap-2 rounded-full bg-danger/10 px-4 py-2.5 text-sm text-danger" role="alert">
                <span class="size-1.5 rounded-full bg-danger"></span><fmt:message key="admin.login.failed"/>
            </p>
        </c:if>

        <form method="post" action="${ctx}/admin/login" data-validate class="mt-8 space-y-5">
            <label class="block">
                <span class="field-label"><fmt:message key="admin.login.username"/></span>
                <input type="text" name="username" class="input" required autocomplete="username" value="<c:out value="${username}"/>">
            </label>
            <label class="block">
                <span class="field-label"><fmt:message key="admin.login.password"/></span>
                <input type="password" name="password" class="input" required autocomplete="current-password">
            </label>
            <button type="submit" class="btn btn-primary w-full"><fmt:message key="admin.login.submit"/></button>
        </form>

        <p class="mt-6 text-center text-xs text-muted/70"><fmt:message key="admin.login.hint"/></p>
    </div>

</t:admin-layout>
