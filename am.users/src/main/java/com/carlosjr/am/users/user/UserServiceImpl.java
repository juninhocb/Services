package com.carlosjr.am.users.user;

import com.carlosjr.am.users.exceptions.ResourceNotFoundException;
import com.carlosjr.am.users.roles.RolesServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Primary
public class UserServiceImpl implements UserService {

    @Value("${com.carlosjr.config.access-token-expiration}")
    private Long accessTokenExpiresIn;
    private final UserRepository userRepository;
    private final RolesServiceImpl rolesServiceImpl;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
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
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
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

        Optional<User> userOpt = userRepository
                .getUserByUsername(username);

        if(userOpt.isEmpty()){
            throw new ResourceNotFoundException("Resource with username " + username
                    + " was not found.");
        }

        return userMapper.userToUserDto(userOpt.get());
    }
    @Override
    public User findPersistedUserByEmail(String email) {

        Optional<User> persistedUserOpt = userRepository
                .getUserByEmail(email);

        if (persistedUserOpt.isEmpty()){
            throw new ResourceNotFoundException("The the expected resource " +
                    "with email " + email + " was not found.");
        }

        return persistedUserOpt.get();
    }

    @Override
    public UserTokenDto userSignIn(String username) {

        Optional<User> userOpt = userRepository
                .getUserByUsername(username);

        if (userOpt.isEmpty()){
            throw new ResourceNotFoundException("Resource with username " + username
                    + " was not found.");
        }

        LocalDateTime currentDateTime = LocalDateTime.now();

        User refreshUser = userOpt.get();

        String temporaryAccessToken = UUID.randomUUID().toString();
        String temporaryRefreshToken = UUID.randomUUID().toString();

        refreshUser.setCurrentAccessToken(temporaryAccessToken);
        refreshUser.setCurrentRefreshToken(temporaryRefreshToken);
        refreshUser.setCreatedAccessToken(currentDateTime);
        refreshUser.setCreatedRefreshToken(currentDateTime);

        userRepository.save(refreshUser);

        return UserTokenDto.builder()
                .accessToken(temporaryAccessToken)
                .refreshToken(temporaryRefreshToken)
                .expiresIn(accessTokenExpiresIn)
                .build();
    }

    @Override
    public boolean validateUserAccessToken(String username, String accessToken) {

        if (accessToken.equals("")){
            return false;
        }

        Optional<User> userOpt = userRepository
                .getUserByUsername(username);

        if (userOpt.isEmpty()){
            throw new ResourceNotFoundException("Resource with username " + username
                    + " was not found.");
        }

        return userOpt.get().getCurrentAccessToken() != null;
    }

    @Override
    public Set<User> getLoggedUsers() {
        return new HashSet<>(userRepository.listOfLoggedUsers());
    }
    public void updateUserDirectly(User user){
        userRepository.save(user);
    }

    @Override
    public boolean validateUserLoggedIn(String username) {
        //TODO: refactor this to take only user at database if access token is not expired
        Optional<User> userOptional = userRepository
                .getUserByUsername(username);
        if (userOptional.isPresent()){
            if (userOptional.get().getCurrentAccessToken() != null){
                return true;
            }
        }
        return false;
    }

}
