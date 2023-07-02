package com.carlosjr.am.users.roles;

import com.carlosjr.am.users.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
public class RolesServiceImpl implements RolesService {
    private final RolesRepository rolesRepository;
    @Override
    public Roles getRoleById(Long id){
        return rolesRepository.getReferenceById(id);
    }
    @Override
    public long getRepositoryCount(){
        return rolesRepository.count();
    }
    @Override
    public void mockRoles(List<Roles> roles){
        rolesRepository.saveAll(roles);
    }
    @Override
    public Set<Roles> findBasicRoles(){
        return rolesRepository.findBasicRoles();
    }
    @Override
    public Set<Roles> findAllRoles(){
        return rolesRepository.findAll().stream().collect(Collectors.toSet());
    }
    @Override
    public boolean isAdmin(User user){
        return user.getRoles().size() == 4;
    }
}
