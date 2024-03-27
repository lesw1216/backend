package com.swoo.fitlog.api.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RestApiResponse<T>{
    private Integer code;
    private HttpStatus httpStatus;
    private String message;
    private T data;
}
