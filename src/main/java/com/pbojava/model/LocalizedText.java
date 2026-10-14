package com.pbojava.model;

/**
 * Teks yang punya versi bahasa Indonesia dan Inggris.
 * Di JSP dipanggil dengan ${portfolio.judul.get(sessionScope.lang)}.
 */
public final class LocalizedText {

    private final String id;
    private final String en;

    public LocalizedText(String id, String en) {
        this.id = id;
        this.en = en;
    }

    /** Mengembalikan teks sesuai bahasa. Bahasa selain "en" memakai versi Indonesia. */
    public String get(String lang) {
        return "en".equals(lang) ? en : id;
    }

    public String getId() {
        return id;
    }

    public String getEn() {
        return en;
    }
}
