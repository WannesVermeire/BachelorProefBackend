package com.example.BachelorProefBackend.UserManagement.Role;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    //GET
    public List<Role> getAllRoles(){return roleRepository.findAll();}

    //POST
    public void addNewRole(Role role){
        log.info("Saving new role {} to the database", role.getName());
        roleRepository.save(role);
    }
}
