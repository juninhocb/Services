package com.carlosjr.am.users.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(value = "/v1/users")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(value = "userId") UUID id){
        return ResponseEntity.ok().body(userService.findUserById(id));
    }
    @PostMapping
    public ResponseEntity<Void> createNewUser(@Valid @RequestBody UserDto userDto, UriComponentsBuilder ucb){
        UUID id = userService.createNewUser(userDto);
        URI resourcePath = ucb
                .path("/v1/users/{userId}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(resourcePath).build();
    }
    @PutMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateUser(@Valid @RequestBody UserDto userDto, @PathVariable(name = "userId") UUID id){
        userService.updateUser(userDto, id);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(name = "userId") UUID id){
        userService.deleteUser(id);
    }

    @PutMapping("/roles/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateUserRoles(@PathVariable(name = "userId") UUID id,
                                @RequestParam(name = "isAdmin") Boolean isAdmin){
        userService.updateRoles(id, isAdmin);
    }

}
