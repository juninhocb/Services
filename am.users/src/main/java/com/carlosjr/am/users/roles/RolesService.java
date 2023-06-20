package com.carlosjr.am.users.roles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RolesService {

    @Autowired
    private RolesRepository rolesRepository;
    public Roles getRoleById(Long id){
        return rolesRepository.getReferenceById(id);
    }
    public long getRepositoryCount(){
        return rolesRepository.count();
    }
    public void mockRoles(List<Roles> roles){
        rolesRepository.saveAll(roles);
    }
    public Set<Roles> findBasicRoles(){
        return rolesRepository.findBasicRoles();
    }
    public Set<Roles> findAllRoles(){
        return rolesRepository.findAll().stream().collect(Collectors.toSet());
    }

}
