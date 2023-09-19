package com.example.democlient.event.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserModel {
    private String userId;
    private String phone;
    private String region;
}
