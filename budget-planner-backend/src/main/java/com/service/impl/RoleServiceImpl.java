package com.service.impl;

import com.model.Role;
import com.model.exceptions.ResourceNotFoundException;
import com.repository.RoleRepository;
import com.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

   private final RoleRepository roleRepository;

   @Override
   public Role getRole(Long id) {
      return this.roleRepository.findById(id)
              .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id.toString()));
   }

   @Override
   public Role createRole(String name) {
      Role role = new Role(name);
      return this.roleRepository.save(role);
   }

   @Override
   public Role getRoleByName(String name) {
      return this.roleRepository.findByName(name)
              .orElseThrow(() -> new ResourceNotFoundException("Role", "name", name));
   }

   @Override
   public List<Role> getAllRoles() {
      return this.roleRepository.findAll();
   }
}
