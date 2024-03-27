package com.swoo.fitlog.api.http;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Builder
public class ErrorResponse {
    private int code;
    private HttpStatus httpStatus;
    private String message;
    private List<Integer> errorCodes;

    public static ErrorResponse of(int code, HttpStatus httpStatus, String message, List<Integer> errorCodes) {
        return ErrorResponse.builder()
                .code(code)
                .httpStatus(httpStatus)
                .message(message)
                .errorCodes(errorCodes)
                .build();
    }
}
