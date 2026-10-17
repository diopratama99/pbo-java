package com.pbojava.dao.memory;

import com.pbojava.dao.TeamDAO;
import com.pbojava.model.LocalizedText;
import com.pbojava.model.TeamMember;

import java.util.List;

/**
 * Data anggota tim.
 * GANTI isi pendidikan, posisi, dan pengalaman dengan data asli.
 * Foto: simpan PNG tanpa background di src/main/webapp/assets/img/team/ dengan nama file di bawah.
 * Selama file PNG-nya belum ada, halaman menampilkan siluet placeholder.
 */
public class InMemoryTeamDAO implements TeamDAO {

    private final List<TeamMember> members = List.of(
            new TeamMember("Dio", "/assets/img/team/dio.png",
                    text("Back End Developer", "Back End Developer"),
                    text("S1 Teknik Informatika", "Bachelor of Informatics Engineering"),
                    List.of(
                            text("Membangun REST API untuk aplikasi kasir (contoh)",
                                    "Built a REST API for a cashier app (example)"),
                            text("Mendesain database untuk sistem presensi (contoh)",
                                    "Designed the database for an attendance system (example)"))),
            new TeamMember("Agmar", "/assets/img/team/agmar.png",
                    text("Software Engineer", "Software Engineer"),
                    text("S1 Teknik Informatika", "Bachelor of Informatics Engineering"),
                    List.of(
                            text("5 tahun pengalaman sebagai Front End Developer dan Fullstack Developer di perusahaan fintech",
                                    "5 years of experience as a Front End Developer and Fullstack Developer at a fintech company"))),
            new TeamMember("Wulandari", "/assets/img/team/wulandari.png",
                    text("UI/UX Designer", "UI/UX Designer"),
                    text("S1 Teknik Informatika", "Bachelor of Informatics Engineering"),
                    List.of(
                            text("Mendesain aplikasi mobile untuk UMKM (contoh)",
                                    "Designed a mobile app for small businesses (example)"),
                            text("Melakukan riset pengguna untuk website kampus (contoh)",
                                    "Ran user research for a campus website (example)"))));

    @Override
    public List<TeamMember> findAll() {
        return members;
    }

    private static LocalizedText text(String id, String en) {
        return new LocalizedText(id, en);
    }
}
