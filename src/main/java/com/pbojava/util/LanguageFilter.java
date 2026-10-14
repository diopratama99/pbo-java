package com.pbojava.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.jsp.jstl.core.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Menentukan bahasa (id/en) untuk setiap request.
 *
 * Urutan prioritas: parameter ?lang= → cookie "lang" → default "id".
 * Hasilnya disimpan di session ("lang") dan dipakai sebagai locale JSTL,
 * sehingga <fmt:message> dan <fmt:formatNumber> mengikuti bahasa yang dipilih.
 */
@WebFilter("/*")
public class LanguageFilter extends HttpFilter {

    public static final String DEFAULT_LANG = "id";
    private static final Set<String> SUPPORTED = Set.of("id", "en");
    private static final String COOKIE_NAME = "lang";
    private static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 365; // 1 tahun

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if (path.startsWith("/assets/")) {
            chain.doFilter(request, response);
            return;
        }

        String fromParam = request.getParameter("lang");
        if (isSupported(fromParam) && "GET".equals(request.getMethod())) {
            // Simpan pilihan, lalu kembali ke URL yang sama tanpa ?lang= supaya URL tetap bersih
            saveCookie(request, response, fromParam);
            request.getSession().setAttribute("lang", fromParam);
            response.sendRedirect(buildUrl(request, null));
            return;
        }

        String lang = DEFAULT_LANG;
        String fromCookie = readCookie(request);
        if (isSupported(fromCookie)) {
            lang = fromCookie;
        }

        request.getSession().setAttribute("lang", lang);
        Config.set(request, Config.FMT_LOCALE, Locale.forLanguageTag(lang));

        // Link untuk tombol ID / EN di navbar, query lain (misalnya ?ticket=) tetap dibawa
        request.setAttribute("langUrlId", buildUrl(request, "id"));
        request.setAttribute("langUrlEn", buildUrl(request, "en"));

        chain.doFilter(request, response);
    }

    private static boolean isSupported(String lang) {
        return lang != null && SUPPORTED.contains(lang);
    }

    private static String readCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private static void saveCookie(HttpServletRequest request, HttpServletResponse response, String lang) {
        Cookie cookie = new Cookie(COOKIE_NAME, lang);
        cookie.setMaxAge(COOKIE_MAX_AGE);
        cookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * URL request saat ini tanpa parameter lang, lalu ditambah lang=... kalau newLang diisi.
     * Hanya memakai query string di URL, jadi isi form POST (misalnya email) tidak ikut masuk ke link.
     */
    private static String buildUrl(HttpServletRequest request, String newLang) {
        List<String> query = new ArrayList<>();
        String queryString = request.getQueryString();
        if (queryString != null) {
            for (String pair : queryString.split("&")) {
                if (!pair.isEmpty() && !pair.equals("lang") && !pair.startsWith("lang=")) {
                    query.add(pair);
                }
            }
        }
        if (newLang != null) {
            query.add("lang=" + newLang);
        }
        String uri = request.getRequestURI();
        return query.isEmpty() ? uri : uri + "?" + String.join("&", query);
    }
}
