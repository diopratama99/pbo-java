package com.pbojava.model;

import java.util.Arrays;
import java.util.List;

/** Satu proyek yang ditampilkan di etalase portofolio. */
public class Portfolio {

    private final int id;
    private final LocalizedText judul;
    private final LocalizedText deskripsi;
    private final String techStack;
    private final String imageUrl;

    public Portfolio(int id, LocalizedText judul, LocalizedText deskripsi, String techStack, String imageUrl) {
        this.id = id;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.techStack = techStack;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public LocalizedText getJudul() {
        return judul;
    }

    public LocalizedText getDeskripsi() {
        return deskripsi;
    }

    /** Tech stack dalam satu teks, dipisah koma. Contoh: "Java, MySQL". */
    public String getTechStack() {
        return techStack;
    }

    /** Tech stack yang sudah dipecah per item, untuk ditampilkan sebagai badge. */
    public List<String> getTechStackList() {
        return Arrays.stream(techStack.split(","))
                .map(String::trim)
                .filter(item -> !item.isEmpty())
                .toList();
    }

    /** Path gambar relatif terhadap context path, contoh: "/assets/img/portfolio/kasir.svg". */
    public String getImageUrl() {
        return imageUrl;
    }
}
