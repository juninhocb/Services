package com.carlosjr.am.users.user;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Void> createNewUser(@RequestBody UserDto userDto, UriComponentsBuilder ucb){
        Long id = userService.createNewUser(userDto);
        URI resourcePath = ucb
                .path("/v1/users/{userId}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(resourcePath).build();
    }



}
