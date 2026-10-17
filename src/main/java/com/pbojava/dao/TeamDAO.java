package com.pbojava.dao;

import com.pbojava.model.TeamMember;

import java.util.List;

public interface TeamDAO {

    List<TeamMember> findAll();
}
