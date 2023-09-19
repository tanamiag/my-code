package com.example.democlient.validation.controller;

import com.example.democlient.validation.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ValidationController {
    private final ValidationService service;

    @GetMapping("/products")
    public void getProductList() {
        final ValidationService.ProductGetAllCommand command = ValidationService.ProductGetAllCommand.builder()
                .prdId(null)
                .prdNm(null)
                .isVIP(true)
                .hasAuth(false)
                .build();

        // 메소드 실행 직전 어드바이저에서 null 체크
        // 어드바이저에서 예외 발생 시 해당 메서드는 호출되지 않음
        service.getProductList(command);
    }
}

