package com.pbojava.dao.memory;

import com.pbojava.dao.AdminDAO;
import com.pbojava.model.Admin;

import java.util.Optional;

/** Satu akun admin dummy untuk prototype: admin / admin123. */
public class InMemoryAdminDAO implements AdminDAO {

    private final Admin admin = new Admin("admin", "Admin Bisa.", "admin123");

    @Override
    public Optional<Admin> findByUsername(String username) {
        return admin.getUsername().equals(username) ? Optional.of(admin) : Optional.empty();
    }
}
