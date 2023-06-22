package com.carlosjr.am.users.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/v1/users")
public class UserResource {
    @Autowired
    private UserService userService;
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "userId") Long id){
        return ResponseEntity.ok().body(userService.findUserById(id));
    }
    @PostMapping
    public ResponseEntity<Void> createNewUser(@Valid @RequestBody UserDto userDto, UriComponentsBuilder ucb){
        Long id = userService.createNewUser(userDto);
        URI resourcePath = ucb
                .path("/v1/users/{userId}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(resourcePath).build();
    }
    @PutMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateUser(@Valid @RequestBody UserDto userDto, @PathVariable(name = "userId") Long id){
        userService.updateUser(id, userDto);
    }


}
