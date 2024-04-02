package com.service;


import com.model.Role;

import java.util.List;

public interface RoleService {

   Role getRole(Long id);

   Role createRole(String name);

   Role getRoleByName(String name);

   List<Role> getAllRoles();
}
