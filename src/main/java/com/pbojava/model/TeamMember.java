package com.pbojava.model;

import java.util.List;

/** Anggota tim yang tampil di halaman profil tim. */
public class TeamMember {

    private final String nama;
    private final String foto;
    private final LocalizedText posisi;
    private final LocalizedText pendidikan;
    private final List<LocalizedText> pengalaman;

    public TeamMember(String nama, String foto, LocalizedText posisi, LocalizedText pendidikan,
                      List<LocalizedText> pengalaman) {
        this.nama = nama;
        this.foto = foto;
        this.posisi = posisi;
        this.pendidikan = pendidikan;
        this.pengalaman = pengalaman;
    }

    public String getNama() {
        return nama;
    }

    /** Path foto PNG relatif terhadap context path, contoh: "/assets/img/team/dio.png". */
    public String getFoto() {
        return foto;
    }

    public LocalizedText getPosisi() {
        return posisi;
    }

    public LocalizedText getPendidikan() {
        return pendidikan;
    }

    public List<LocalizedText> getPengalaman() {
        return pengalaman;
    }
}
