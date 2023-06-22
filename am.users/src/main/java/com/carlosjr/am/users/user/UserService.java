package com.carlosjr.am.users.user;

import com.carlosjr.am.users.exceptions.ResourceNotFoundException;
import com.carlosjr.am.users.roles.RolesService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RolesService rolesService;
    public User findUserById(Long id){
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.orElseThrow(() -> new ResourceNotFoundException(String.format("Resource with id = %d was not found in database", id)));
    }
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
    public Long createNewUser(UserDto userDto){
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
    public void updateUser(Long id, UserDto userDto){
        findUserById(id);
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        userRepository.save(user);
    }
    public void deleteUser(Long id){
        User userDelete = findUserById(id);
        userRepository.delete(userDelete);
    }
    public void updateRoles(Long id, Boolean isAdmin){
        User user = findUserById(id);
        if (isAdmin){
            user.setRoles(rolesService.findAllRoles());
        } else {
            user.setRoles(rolesService.findBasicRoles());
        }
    }
}
