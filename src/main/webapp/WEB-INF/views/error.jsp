<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%-- Sengaja tidak memakai layout.tag: halaman ini harus tetap tampil walaupun penyebab error-nya ada di layout atau database --%>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Terjadi kesalahan | Bisa.</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body class="grid min-h-screen place-items-center px-5">
    <main class="max-w-md text-center">
        <p class="text-2xl font-semibold tracking-tight">Bisa.</p>
        <h1 class="mt-8 text-3xl font-semibold tracking-tight">Maaf, sedang ada gangguan.</h1>
        <p class="mt-3 text-muted">Silakan coba lagi beberapa saat lagi.</p>
        <p class="mt-1 text-sm text-muted">Something went wrong on our side. Please try again in a moment.</p>
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary mt-8">Kembali ke beranda</a>
    </main>
</body>
</html>
