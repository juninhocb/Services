package com.carlosjr.am.users.user;

import java.util.Set;
import java.util.UUID;

public interface UserService {
    UserDto findUserById(UUID id);
    UUID createNewUser(UserDto userDto);
    void updateUser(UserDto newerUserDto, UUID id);
    void deleteUser(UUID id);
    void updateRoles(UUID id, Boolean isAdmin);
    UserDto findUserByUsername(String username);
    User findPersistedUserByEmail(String email);
    UserTokenDto userSignIn(String username);
    boolean validateUserAccessToken(String username, String accessToken);
    Set<User> getLoggedUsers();
    void updateUserDirectly(User user);
    boolean validateUserLoggedIn(String username);

}
