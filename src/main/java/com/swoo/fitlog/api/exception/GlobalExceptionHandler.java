package com.swoo.fitlog.api.exception;

import com.swoo.fitlog.api.enums.ValidErrorCode;
import com.swoo.fitlog.api.http.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
    * @Valid 어노테이션 예외 처리
    *
    * 1. 에러 코드를 담기 위한 리스트 생성
    * 2. 에러 코드 불러오기
    * 3. ErrorResponse 생성
    * */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        List<Integer> errorCodes = new ArrayList<>();

        /*
        * bindingResult에 저장된 필드 에러들의 코드를 사용해 ValidErrorCode에 저장된 에러 코드를 가져온다.
        * */
        exception.getBindingResult().getAllErrors()
                .forEach(error -> errorCodes.add(ValidErrorCode.getErrorCode(Objects.requireNonNull(error.getCode()))));

        ErrorResponse errorResponse =
                ErrorResponse.of(400, HttpStatus.BAD_REQUEST,"예기치 않은 오류가 발생했습니다.", errorCodes);

        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }
}
