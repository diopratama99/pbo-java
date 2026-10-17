package com.pbojava.model;

/** Calon klien yang mengirim pengajuan. */
public class Client {

    private final int id;
    private final String nama;
    private final String instansi;
    private final String email;
    private final String noWa;

    public Client(int id, String nama, String instansi, String email, String noWa) {
        this.id = id;
        this.nama = nama;
        this.instansi = instansi;
        this.email = email;
        this.noWa = noWa;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getInstansi() {
        return instansi;
    }

    public String getEmail() {
        return email;
    }

    public String getNoWa() {
        return noWa;
    }
}
