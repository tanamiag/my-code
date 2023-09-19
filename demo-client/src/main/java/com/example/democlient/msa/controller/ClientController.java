package com.example.democlient.msa.controller;

import com.example.demoserverendpoint.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {
    private final ServerEndpoint sampleEndpoint;

    @GetMapping("/users")
    public List<ServerEndpoint.UserDto> getUserList() {
        ServerEndpoint.UserListParam param = new ServerEndpoint.UserListParam();
        param.setRegion("jeju");
        return sampleEndpoint.userList(param);	// 서비스 인터페이스 메소드 호출
    }

    @GetMapping("/user")
    public ServerEndpoint.UserDto getUser() {
        return sampleEndpoint.user("hjjang");	// 서비스 인터페이스 메소드 호출
    }
}

