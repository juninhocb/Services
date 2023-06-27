package com.carlosjr.am.users.user;

import org.springframework.stereotype.Component;
@Component
public class UserMapper {
    public UserDto userToUserDto(User user){
        return UserDto.builder()
                .fullName(user.getFullName())
                .username(user.getUsername())
                .email(user.getEmail())
                .groupId(user.getGroupId())
                .password(user.getPassword())
                .roles(user.getRoles())
                .active(user.getActive())
                .id(user.getId())
                .build();
    }

    public User userDtoToUser(UserDto olderUser, UserDto newerUser){
        return User.builder()
                .fullName(newerUser.fullName())
                .username(newerUser.username())
                .email(newerUser.email())
                .groupId(newerUser.groupId())
                .password(newerUser.password())
                .roles(olderUser.roles())
                .active(olderUser.active())
                .id(olderUser.id())
                .build();
    }
    public User userDtoToUser(UserDto userDto){
        return User.builder()
                .fullName(userDto.fullName())
                .username(userDto.username())
                .email(userDto.email())
                .groupId(userDto.groupId())
                .password(userDto.password())
                .roles(userDto.roles())
                .active(userDto.active())
                .id(userDto.id())
                .build();
    }


}
