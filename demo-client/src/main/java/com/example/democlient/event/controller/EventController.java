package com.example.democlient.event.controller;

import com.example.democlient.event.model.UserEvent;
import com.example.democlient.event.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class EventController {
    private final ApplicationEventPublisher publisher;

    @GetMapping("/user/create")
    public void createUser() {
        UserModel user = UserModel.builder()
                .userId("HJ")
                .phone("01000000000")
                .region("ansan")
                .build();

        publisher.publishEvent(UserEvent.beforeCreate(user));
        System.out.println(user.getUserId() + "님 회원 추가 완료");    // 회원 정보 추가 로직
        publisher.publishEvent(UserEvent.afterCreate(user));
    }

    @GetMapping("/user/modify")
    public void modifyUser() {
        UserModel before = UserModel.builder()
                .userId("HJ")
                .phone("01000000000")
                .region("ansan")
                .build();
        UserModel after = UserModel.builder()
                .userId("HJ")
                .phone("01011111111")
                .region("seoul")
                .build();

        publisher.publishEvent(UserEvent.beforeModify(before, after));
        System.out.println(after.getUserId() + "님 회원 정보 수정 완료");    // 회원 정보 수정 로직
        publisher.publishEvent(UserEvent.afterModify(before, after));
    }
}

