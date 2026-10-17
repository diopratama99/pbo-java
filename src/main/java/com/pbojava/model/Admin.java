package com.pbojava.model;

/**
 * Akun admin.
 * Prototype: password masih disimpan apa adanya. Di tahap database diganti hash + salt (PasswordUtil).
 */
public class Admin {

    private final String username;
    private final String nama;
    private final String password;

    public Admin(String username, String nama, String password) {
        this.username = username;
        this.nama = nama;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getNama() {
        return nama;
    }

    public boolean passwordCocok(String input) {
        return password.equals(input);
    }
}
