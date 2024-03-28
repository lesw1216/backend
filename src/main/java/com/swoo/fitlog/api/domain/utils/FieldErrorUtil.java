package com.swoo.fitlog.api.domain.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FieldErrorUtil {

    private static final Map<String, Integer> fieldErrors = new HashMap<>();

    public FieldErrorUtil() {
        fieldErrors.put("email", ErrorCode.EMAIL_FIELD_ERROR);
        fieldErrors.put("password", ErrorCode.PASSWORD_FIELD_ERROR);
        fieldErrors.put("otp", ErrorCode.OTP_FIELD_ERROR);
    }

    public int getErrorCode(String field) {
        return fieldErrors.get(field);
    }
}
