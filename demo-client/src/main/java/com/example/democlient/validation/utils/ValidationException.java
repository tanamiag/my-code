package com.example.democlient.validation.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Getter
@ToString
@AllArgsConstructor
public class ValidationException extends RuntimeException {
    private final Validatable.ValidateResult validateResult;

    @Override
    public String getMessage() {
        return StringUtils.join(this.validateResult.getMessageList(), "\n");
    }
}
