package com.example.demoserverendpoint;


import com.example.democore.msa.annotations.MicroServiceEndpoint;
import lombok.*;

import java.util.List;

@MicroServiceEndpoint("api-server")
public interface ServerEndpoint {
    List<UserDto> userList(UserListParam param);

    UserDto user(String userId);

    @Data
    public static class UserListParam {
        private String region;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class UserDto {
        private String userId;
        private String phone;
        private String region;
    }
}
