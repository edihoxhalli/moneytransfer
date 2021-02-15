package com.edi.moneytransfer.rest;

import com.edi.moneytransfer.application.dto.UserDto;
import com.edi.moneytransfer.application.mapper.UserMapper;
import com.edi.moneytransfer.application.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/user")
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto){
        return userService.createUser(userDto);
    }

    @PutMapping
    public String login(@RequestBody @Valid UserDto userDto){
        userService.login(userDto);
        return "Login successful!";
    }

    @GetMapping
    public UserDto getUser(){
        return userMapper.toDto(userService.getCurrentlyLoggedInUser());
    }
}
