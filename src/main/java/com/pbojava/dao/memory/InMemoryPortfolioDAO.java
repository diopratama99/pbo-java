package com.pbojava.dao.memory;

import com.pbojava.dao.PortfolioDAO;
import com.pbojava.model.LocalizedText;
import com.pbojava.model.Portfolio;

import java.util.List;
import java.util.Optional;

/** Data portofolio contoh untuk prototype. Diganti dengan data dari database di tahap berikutnya. */
public class InMemoryPortfolioDAO implements PortfolioDAO {

    private final List<Portfolio> portfolios = List.of(
            new Portfolio(1,
                    new LocalizedText("Sistem Kasir UMKM", "Point of Sale for Small Shops"),
                    new LocalizedText(
                            "Aplikasi kasir berbasis web untuk mencatat penjualan dan stok barang harian.",
                            "A web-based cashier app to record daily sales and stock."),
                    "Java, MySQL, Tailwind CSS",
                    "/assets/img/portfolio/kasir.svg"),
            new Portfolio(2,
                    new LocalizedText("Presensi Kampus", "Campus Attendance"),
                    new LocalizedText(
                            "Presensi mahasiswa dengan QR code, lengkap dengan rekap per mata kuliah.",
                            "Student attendance with QR codes, plus a recap for every course."),
                    "Kotlin, Firebase",
                    "/assets/img/portfolio/presensi.svg"),
            new Portfolio(3,
                    new LocalizedText("Company Profile Kopi Lokal", "Local Coffee Company Profile"),
                    new LocalizedText(
                            "Website profil usaha dengan menu, lokasi toko, dan pemesanan lewat WhatsApp.",
                            "A business website with the menu, store locations, and ordering via WhatsApp."),
                    "PHP, Bootstrap",
                    "/assets/img/portfolio/kopi.svg"));

    @Override
    public List<Portfolio> findAll() {
        return portfolios;
    }

    @Override
    public Optional<Portfolio> findById(int id) {
        return portfolios.stream().filter(portfolio -> portfolio.getId() == id).findFirst();
    }
}
