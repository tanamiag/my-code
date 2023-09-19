package com.example.democlient.validation.utils;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 검증 가능 인터페이스
 * 
 */
public interface Validatable {
    default void validate(ValidateResult validateResult) {}

    @Getter
    @ToString
    class ValidateResult {
        private boolean valid = true;
        private List<String> messageList = new ArrayList<>();

        public boolean hasError() {
            return !valid;
        }

        public void addMessage(String message) {
            this.messageList.add(message);
            valid = false;
        }

        public static ValidateResult newInstance() {
            return new ValidateResult();
        }
    }
}
