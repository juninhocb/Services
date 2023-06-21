package com.carlosjr.am.users.user;

import lombok.Builder;

@Builder
public record UserDto(Long groupId, String fullName, String username, String password, String email) {
}
