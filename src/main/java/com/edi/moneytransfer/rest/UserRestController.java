package com.edi.moneytransfer.rest;

import com.edi.moneytransfer.application.dto.UserDto;
import com.edi.moneytransfer.application.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/user")
@AllArgsConstructor
public class UserRestController {

    private final UserService userService;

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto){
        return userService.createUser(userDto);
    }

    @PutMapping
    public String login(@RequestBody UserDto userDto){
        userService.login(userDto);
        return "Login successful!";
    }
}
