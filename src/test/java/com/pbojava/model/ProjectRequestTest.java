package com.pbojava.model;

import com.pbojava.exception.InvalidStatusException;
import com.pbojava.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Alur status di plan.md bagian 5. */
class ProjectRequestTest {

    private static final String LINK = "https://chat.whatsapp.com/AbCdEf123";

    @Test
    void alurNormalSampaiDisetujui() throws Exception {
        ProjectRequest request = baru(RequestStatus.PENDING);

        request.mulaiReview();
        assertEquals(RequestStatus.REVIEW, request.getStatus());

        request.approve("  " + LINK + "  ");
        assertEquals(RequestStatus.APPROVED, request.getStatus());
        assertEquals(LINK, request.getWagLink());
    }

    @Test
    void bolehDitolakLangsungDariPending() throws Exception {
        ProjectRequest request = baru(RequestStatus.PENDING);

        request.reject("Spam");
        assertEquals(RequestStatus.REJECTED, request.getStatus());
        assertEquals("Spam", request.getAlasanDitolak());
    }

    @Test
    void tidakBisaDisetujuiSebelumDireview() {
        ProjectRequest request = baru(RequestStatus.PENDING);

        assertThrows(InvalidStatusException.class, () -> request.approve(LINK));
        assertEquals(RequestStatus.PENDING, request.getStatus());
    }

    @Test
    void statusAkhirTidakBisaDiubah() {
        ProjectRequest approved = baru(RequestStatus.APPROVED);
        ProjectRequest rejected = baru(RequestStatus.REJECTED);

        assertThrows(InvalidStatusException.class, approved::mulaiReview);
        assertThrows(InvalidStatusException.class, () -> approved.reject("Batal"));
        assertThrows(InvalidStatusException.class, () -> rejected.approve(LINK));
        assertThrows(InvalidStatusException.class, rejected::mulaiReview);
    }

    @Test
    void linkGrupHarusLinkWhatsApp() {
        ProjectRequest request = baru(RequestStatus.REVIEW);

        assertThrows(ValidationException.class, () -> request.approve("https://example.com/grup"));
        assertThrows(ValidationException.class, () -> request.approve(ProjectRequest.WAG_PREFIX));
        assertThrows(ValidationException.class, () -> request.approve(null));
        assertEquals(RequestStatus.REVIEW, request.getStatus());
        assertNull(request.getWagLink());
    }

    @Test
    void alasanPenolakanWajibDiisi() {
        ProjectRequest request = baru(RequestStatus.REVIEW);

        assertThrows(ValidationException.class, () -> request.reject("   "));
        assertThrows(ValidationException.class, () -> request.reject("x".repeat(256)));
        assertEquals(RequestStatus.REVIEW, request.getStatus());
    }

    private static ProjectRequest baru(RequestStatus status) {
        Client client = new Client(1, "Rina", null, "klien@contoh.com", "081234567890");
        return new ProjectRequest(1, "DA-TEST01", client, List.of(), null,
                BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN, status, null, null, LocalDateTime.now());
    }
}
