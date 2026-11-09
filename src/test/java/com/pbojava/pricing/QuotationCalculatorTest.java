package com.pbojava.pricing;

import com.pbojava.model.LocalizedText;
import com.pbojava.model.feature.CoreFeature;
import com.pbojava.model.feature.DatabaseFeature;
import com.pbojava.model.feature.Feature;
import com.pbojava.model.feature.IntegrationFeature;
import com.pbojava.model.feature.PlatformFeature;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Angka-angkanya sama dengan contoh di plan.md bagian 6 dan data di db/seed.sql. */
class QuotationCalculatorTest {

    private static final PlatformFeature WEBSITE = new PlatformFeature(1, text("Website"), text(""), rp(3_000_000), new BigDecimal("1.00"), true);
    private static final PlatformFeature MOBILE = new PlatformFeature(2, text("Mobile"), text(""), rp(5_000_000), new BigDecimal("1.30"), true);
    private static final PlatformFeature WEB_MOBILE = new PlatformFeature(3, text("Web + Mobile"), text(""), rp(7_000_000), new BigDecimal("1.60"), true);
    private static final DatabaseFeature MYSQL = new DatabaseFeature(4, text("MySQL"), text(""), rp(1_000_000), true);
    private static final CoreFeature LOGIN = new CoreFeature(6, text("Login"), text(""), rp(1_500_000), true);
    private static final CoreFeature DASHBOARD = new CoreFeature(7, text("Dashboard"), text(""), rp(2_500_000), true);
    private static final CoreFeature CHAT = new CoreFeature(8, text("Chat"), text(""), rp(3_000_000), true);
    private static final CoreFeature NOTIFIKASI = new CoreFeature(9, text("Notifikasi"), text(""), rp(1_000_000), true);
    private static final CoreFeature LAPORAN = new CoreFeature(10, text("Laporan"), text(""), rp(1_500_000), true);
    private static final IntegrationFeature PAYMENT = new IntegrationFeature(12, text("Payment"), text(""), rp(2_000_000), rp(500_000), true);
    private static final IntegrationFeature MAPS = new IntegrationFeature(13, text("Maps"), text(""), rp(1_000_000), rp(250_000), true);

    private final QuotationCalculator calculator = QuotationCalculator.standar();

    @Test
    void contohDiPlanMd() {
        Quotation q = calculator.hitung(MOBILE, MYSQL, List.of(LOGIN, CHAT, PAYMENT));

        assertEquals(rp(14_350_000), q.getSubtotal().stripTrailingZeros());
        assertEquals(0, q.getDiskon().signum());
        assertEquals(rp(14_350_000), q.getTotal().stripTrailingZeros());
        // Login 1.500.000 x 1,3 = 1.950.000
        assertEquals(rp(1_950_000), hargaDi(q, LOGIN));
    }

    @Test
    void integrasiTidakDipengaruhiPlatform() {
        Quotation website = calculator.hitung(WEBSITE, MYSQL, List.of(PAYMENT));
        Quotation mobile = calculator.hitung(MOBILE, MYSQL, List.of(PAYMENT));

        assertEquals(rp(2_500_000), hargaDi(website, PAYMENT));
        assertEquals(rp(2_500_000), hargaDi(mobile, PAYMENT));
    }

    @Test
    void limaFiturDapatDiskonBundling() {
        Quotation q = calculator.hitung(WEB_MOBILE, MYSQL, List.of(LOGIN, DASHBOARD, NOTIFIKASI, LAPORAN, MAPS));

        assertEquals(rp(19_650_000), q.getSubtotal().stripTrailingZeros());
        assertEquals(rp(1_965_000), q.getDiskon().stripTrailingZeros());
        assertEquals(rp(17_685_000), q.getTotal().stripTrailingZeros());
    }

    @Test
    void empatFiturBelumDapatDiskon() {
        Quotation q = calculator.hitung(WEBSITE, MYSQL, List.of(LOGIN, DASHBOARD, NOTIFIKASI, MAPS));

        assertEquals(0, q.getDiskon().signum());
    }

    @Test
    void platformDanDatabaseTidakDihitungSebagaiFiturBundling() {
        // 4 fitur + platform + database = 6 item, tapi yang dihitung untuk diskon hanya 4
        List<Feature> fitur = List.of(LOGIN, DASHBOARD, CHAT, PAYMENT);
        assertEquals(0, calculator.hitung(MOBILE, MYSQL, fitur).getDiskon().signum());
    }

    @Test
    void rincianBerisiPlatformDatabaseDanFitur() {
        Quotation q = calculator.hitung(WEBSITE, MYSQL, List.of(LOGIN));

        assertEquals(3, q.getRincian().size());
        // Website 3.000.000 + MySQL 1.000.000 + Login 1.500.000 (x1,0)
        assertEquals(rp(5_500_000), q.getTotal().stripTrailingZeros());
    }

    private static BigDecimal hargaDi(Quotation q, Feature feature) {
        return q.getRincian().stream()
                .filter(item -> item.getFeature() == feature)
                .findFirst()
                .orElseThrow()
                .getHargaSaatItu()
                .stripTrailingZeros();
    }

    private static LocalizedText text(String value) {
        return new LocalizedText(value, value);
    }

    private static BigDecimal rp(long value) {
        return BigDecimal.valueOf(value).stripTrailingZeros();
    }
}
