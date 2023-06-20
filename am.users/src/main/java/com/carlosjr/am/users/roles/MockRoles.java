package com.carlosjr.am.users.roles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class MockRoles implements CommandLineRunner{
    @Autowired
    RolesService rolesService;
    @Override
    public void run(String... args) throws Exception {
        if(rolesService.getRepositoryCount() == 0){
            Roles role1 = Roles.builder().id(1L).isBasic(true).name("write-resources").build();
            Roles role2 = Roles.builder().id(2L).isBasic(true).name("read-resources").build();
            Roles role3 = Roles.builder().id(3L).isBasic(false).name("manage-users").build();
            Roles role4 = Roles.builder().id(4L).isBasic(false).name("manage-system").build();
            rolesService.mockRoles(Arrays.asList(role1, role2, role3, role4));
        }
    }
}
