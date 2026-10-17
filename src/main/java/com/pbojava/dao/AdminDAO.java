package com.pbojava.dao;

import com.pbojava.model.Admin;

import java.util.Optional;

public interface AdminDAO {

    Optional<Admin> findByUsername(String username);
}
