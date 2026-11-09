package com.pbojava.model;

import com.pbojava.util.PasswordUtil;

/**
 * Akun admin. Password tidak pernah disimpan apa adanya, hanya hash dan salt-nya (PasswordUtil).
 */
public class Admin {

    private final int id;
    private final String username;
    private final String nama;
    private final String passwordHash;
    private final String salt;

    public Admin(int id, String username, String nama, String passwordHash, String salt) {
        this.id = id;
        this.username = username;
        this.nama = nama;
        this.passwordHash = passwordHash;
        this.salt = salt;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getNama() {
        return nama;
    }

    public boolean passwordCocok(String input) {
        return PasswordUtil.cocok(input, passwordHash, salt);
    }
}
