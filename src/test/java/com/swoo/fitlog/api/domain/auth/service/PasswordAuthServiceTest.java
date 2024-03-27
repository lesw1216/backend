package com.swoo.fitlog.api.domain.auth.service;

import com.swoo.fitlog.api.exception.ExpiredPasswordException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PasswordAuthServiceTest {

    @Autowired
    private PasswordAuthService passwordAuthService;

    @Test
    @DisplayName("비밀 번호 임시 저장 성공")
    void success_save_password() {
        // given
        String email = "abc@naver.com";
        String password = "123456";

        // when
        passwordAuthService.verifyPassword(email, password);

        // then
        assertTrue(passwordAuthService.certifyPassword(email, password));
    }

    @Test
    @DisplayName("임시 비밀 번호 유효시간 만료")
    void expire_temp_password() {
        // given
        String email = "test2@naver.com";
        String password = "123456";

        // when
        // then
        assertThrows(ExpiredPasswordException.class, () -> passwordAuthService.certifyPassword(email, password));
    }

    @Test
    @DisplayName("저장된 비밀 번호 불일치")
    void not_equal_password() {
        // given
        String email = "test3@naver.com";
        String password = "123456";

        // when
        passwordAuthService.verifyPassword(email, password);

        // then
        assertFalse(passwordAuthService.certifyPassword(email, "111111"));
    }
}