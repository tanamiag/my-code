package com.example.democlient.validation.service;

import com.example.democlient.validation.utils.Validatable;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {
    public void getProductList(ProductGetAllCommand command) {
        System.out.println("getProductList 호출");
    }

    @Getter
    @Builder
    public static class ProductGetAllCommand implements Validatable {
        private String prdId;
        private String prdNm;
        private Boolean isVIP;
        private Boolean hasAuth;
        @Override
        public void validate(ValidateResult validateResult) {
            if (StringUtils.isEmpty(prdId)) {
                validateResult.addMessage("상품ID를 입력해주세요.");
            }
            if (StringUtils.isEmpty(prdNm)) {
                validateResult.addMessage("상품명을 입력해주세요.");
            }
            // 파라미터 내의 여러 필드를 함께 검증
            if (isVIP && !hasAuth) {
                validateResult.addMessage("VIP는 권한이 있어야합니다.");
            }
        }
    }
}
