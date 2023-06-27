package com.carlosjr.am.users.user;

import com.carlosjr.am.users.exceptions.ResourceNotFoundException;
import com.carlosjr.am.users.roles.RolesService;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RolesService rolesService;
    public User findUserById(UUID id){
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.orElseThrow(() -> new ResourceNotFoundException(String.format("Resource with id = %s was not found in database", id)));
    }
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
    public UUID createNewUser(UserDto userDto){
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setRoles(rolesService.findBasicRoles());
        user.setActive(true);
        User userSaved = userRepository.save(user);
        return userSaved.getId();
    }
    public void updateUser(UUID id, UserDto userDto){
        User user = findUserById(id);
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
        userRepository.save(user);
    }
}
