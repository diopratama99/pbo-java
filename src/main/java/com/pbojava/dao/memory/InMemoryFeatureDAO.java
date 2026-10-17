package com.pbojava.dao.memory;

import com.pbojava.dao.FeatureDAO;
import com.pbojava.model.LocalizedText;
import com.pbojava.model.feature.CoreFeature;
import com.pbojava.model.feature.DatabaseFeature;
import com.pbojava.model.feature.Feature;
import com.pbojava.model.feature.IntegrationFeature;
import com.pbojava.model.feature.PlatformFeature;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/** Data fitur contoh, isinya sama dengan db/seed.sql ditambah versi bahasa Inggris. */
public class InMemoryFeatureDAO implements FeatureDAO {

    private final List<Feature> features = List.of(
            new PlatformFeature(1, text("Website", "Website"),
                    text("Aplikasi diakses lewat browser", "Accessed through a web browser"),
                    rp("3000000"), new BigDecimal("1.00"), true),
            new PlatformFeature(2, text("Aplikasi Mobile", "Mobile App"),
                    text("Aplikasi Android/iOS", "An Android/iOS app"),
                    rp("5000000"), new BigDecimal("1.30"), true),
            new PlatformFeature(3, text("Website + Mobile", "Website + Mobile"),
                    text("Versi web dan aplikasi mobile", "Both a web version and a mobile app"),
                    rp("7000000"), new BigDecimal("1.60"), true),

            new DatabaseFeature(4, text("MySQL", "MySQL"),
                    text("Database relasional yang umum dipakai", "A widely used relational database"),
                    rp("1000000"), true),
            new DatabaseFeature(5, text("PostgreSQL", "PostgreSQL"),
                    text("Database relasional untuk data yang kompleks", "A relational database for complex data"),
                    rp("1000000"), true),

            new CoreFeature(6, text("Login & Registrasi", "Login & Sign Up"),
                    text("Pengguna bisa daftar dan masuk ke akun", "Users can sign up and log in"),
                    rp("1500000"), true),
            new CoreFeature(7, text("Dashboard Admin", "Admin Dashboard"),
                    text("Halaman pengelolaan data untuk pemilik", "A page for owners to manage data"),
                    rp("2500000"), true),
            new CoreFeature(8, text("Chat", "Chat"),
                    text("Pengguna bisa saling berkirim pesan", "Users can message each other"),
                    rp("3000000"), true),
            new CoreFeature(9, text("Notifikasi", "Notifications"),
                    text("Pemberitahuan ke pengguna", "Alerts sent to users"),
                    rp("1000000"), true),
            new CoreFeature(10, text("Laporan & Export PDF", "Reports & PDF Export"),
                    text("Rekap data yang bisa diunduh", "Downloadable data summaries"),
                    rp("1500000"), true),
            new CoreFeature(11, text("Pencarian & Filter", "Search & Filter"),
                    text("Cari dan saring data dengan cepat", "Find and filter data quickly"),
                    rp("1000000"), true),
            // Contoh fitur nonaktif: tidak muncul di kalkulator, tapi masih ada di halaman admin
            new CoreFeature(16, text("Live Streaming", "Live Streaming"),
                    text("Siaran video langsung", "Live video broadcasts"),
                    rp("6000000"), false),

            new IntegrationFeature(12, text("Payment Gateway", "Payment Gateway"),
                    text("Pembayaran online (transfer, e-wallet)", "Online payments (bank transfer, e-wallet)"),
                    rp("2000000"), rp("500000"), true),
            new IntegrationFeature(13, text("Google Maps", "Google Maps"),
                    text("Menampilkan peta dan lokasi", "Show maps and locations"),
                    rp("1000000"), rp("250000"), true),
            new IntegrationFeature(14, text("Notifikasi WhatsApp", "WhatsApp Notifications"),
                    text("Kirim pesan otomatis ke WhatsApp", "Send automatic WhatsApp messages"),
                    rp("1000000"), rp("300000"), true),
            new IntegrationFeature(15, text("Login dengan Google", "Sign in with Google"),
                    text("Masuk pakai akun Google", "Log in with a Google account"),
                    rp("750000"), rp("250000"), true));

    @Override
    public List<Feature> findAll() {
        return features;
    }

    @Override
    public List<Feature> findActive() {
        return features.stream().filter(Feature::isAktif).toList();
    }

    @Override
    public Optional<Feature> findById(int id) {
        return features.stream().filter(feature -> feature.getId() == id).findFirst();
    }

    private static LocalizedText text(String id, String en) {
        return new LocalizedText(id, en);
    }

    private static BigDecimal rp(String nilai) {
        return new BigDecimal(nilai);
    }
}
