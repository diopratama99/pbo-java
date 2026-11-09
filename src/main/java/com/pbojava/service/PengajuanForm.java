package com.pbojava.service;

import java.util.List;

/** Isian form kalkulator estimasi, apa adanya dari request. Validasinya ada di RequestService. */
public record PengajuanForm(String platformId, String databaseId, List<String> featureIds, String notes,
                            String nama, String instansi, String noWa, String email) {
}
