package com.carlosjr.am.users.user;

import com.carlosjr.am.users.exceptions.ResourceNotFoundException;
import com.carlosjr.am.users.roles.RolesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RolesService rolesService;
    private final UserMapper userMapper;
    public UserDto findUserById(UUID id){
        Optional<User> userOpt = userRepository.findById(id);
        return userMapper.userToUserDto(userOpt.orElseThrow(() -> new ResourceNotFoundException(String.format("Resource with id = %s was not found in database", id))));
    }
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
    public UUID createNewUser(UserDto userDto){
        User user = userMapper.userDtoToUser(userDto);
        user.setRoles(rolesService.findBasicRoles());
        user.setActive(true);
        User userSaved = userRepository.save(user);
        return userSaved.getId();
    }
    public void updateUser(UserDto newerUserDto, UUID id){
        UserDto olderUserDto = findUserById(id);
        userRepository.save(userMapper.userDtoToUser(olderUserDto, newerUserDto));
    }
    public void deleteUser(UUID id){
        userRepository.delete(userMapper.userDtoToUser(findUserById(id)));
    }
    public void updateRoles(UUID id, Boolean isAdmin){
        User user = userMapper.userDtoToUser(findUserById(id));
        if (isAdmin){
            user.setRoles(rolesService.findAllRoles());
        } else {
            user.setRoles(rolesService.findBasicRoles());
        }
        userRepository.save(user);
    }
    public User findUserByUsername(String username){
        return userRepository.getUserByUsername(username);
    }

}
