package com.example.demoserver.controller;

import com.example.demoserverendpoint.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/server")
@RequiredArgsConstructor
public class ServerController implements ServerEndpoint {

    @Override
    @GetMapping("")
    public List<UserDto> userList(UserListParam param) {
        final List<UserDto> list = new ArrayList<>();
        UserDto user1 = UserDto.builder().userId("userA").region(param.getRegion()).phone("01011110000").build();
        UserDto user2 = UserDto.builder().userId("userB").region(param.getRegion()).phone("01022220000").build();
        list.add(user1);
        list.add(user2);
        return list;
    }

    @GetMapping("/{userId}")
    @Override
    public UserDto user(@PathVariable String userId) {
        UserDto user3 = UserDto.builder().userId(userId).region("ansan").phone("01033330000").build();
        return user3;
    }
}
