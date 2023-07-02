package com.carlosjr.am.users.user;

import com.carlosjr.am.users.exceptions.ResourceNotFoundException;
import com.carlosjr.am.users.roles.RolesServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Primary
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RolesServiceImpl rolesServiceImpl;
    private final UserMapper userMapper;
    @Override
    public UserDto findUserById(UUID id){
        Optional<User> userOpt = userRepository.findById(id);
        return userMapper.userToUserDto(userOpt.orElseThrow(() -> new ResourceNotFoundException(String.format("Resource with id = %s was not found in database", id))));
    }
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
    @Override
    public UUID createNewUser(UserDto userDto){
        User user = userMapper.userDtoToUser(userDto);
        user.setRoles(rolesServiceImpl.findBasicRoles());
        user.setActive(true);
        User userSaved = userRepository.save(user);
        return userSaved.getId();
    }
    @Override
    public void updateUser(UserDto newerUserDto, UUID id){
        UserDto olderUserDto = findUserById(id);
        userRepository.save(userMapper.userDtoToUser(olderUserDto, newerUserDto));
    }
    @Override
    public void deleteUser(UUID id){
        userRepository.delete(userMapper.userDtoToUser(findUserById(id)));
    }
    @Override
    public void updateRoles(UUID id, Boolean isAdmin){
        User user = userMapper.userDtoToUser(findUserById(id));
        if (isAdmin){
            user.setRoles(rolesServiceImpl.findAllRoles());
        } else {
            user.setRoles(rolesServiceImpl.findBasicRoles());
        }
        userRepository.save(user);
    }
    @Override
    public UserDto findUserByUsername(String username){
        return userMapper.userToUserDto(userRepository.getUserByUsername(username));
    }

}
