package com.carlosjr.am.users.roles;

import com.carlosjr.am.users.user.User;

import java.util.List;
import java.util.Set;

public interface RolesService
{
    Roles getRoleById(Long id);
    long getRepositoryCount();
    void mockRoles(List<Roles> roles);
    Set<Roles> findBasicRoles();
    Set<Roles> findAllRoles();
    boolean isAdmin(User user);

}
