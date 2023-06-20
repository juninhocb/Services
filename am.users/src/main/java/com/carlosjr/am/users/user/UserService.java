package com.carlosjr.am.users.user;

import com.carlosjr.am.users.roles.Roles;
import com.carlosjr.am.users.roles.RolesService;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RolesService rolesService;
    public User findUserById(UUID id){
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.orElseThrow();
    }
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
    public UUID createNewUser(UserDto userDto){
        try{
            User user = new User();
            BeanUtils.copyProperties(userDto, user);
            user.setRoles(rolesService.findBasicRoles());
            user.setActive(true);
            User userSaved = userRepository.save(user);
            return userSaved.getId();
        }catch (Exception ex){
            System.out.println(ex.getCause());
        }
        return null;
    }

    public void updateUser(UUID id, UserDto userDto){
        findUserById(id);
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        userRepository.save(user);
    }
    public void deleteUser(UUID id){
        User userDelete = findUserById(id);
        userRepository.delete(userDelete);
    }
    public void updateRoles(UUID id, Boolean isAdmin){
        User user = findUserById(id);
        if (isAdmin){
            user.setRoles(rolesService.findAllRoles());
        } else {
            user.setRoles(rolesService.findBasicRoles());
        }
    }
}
