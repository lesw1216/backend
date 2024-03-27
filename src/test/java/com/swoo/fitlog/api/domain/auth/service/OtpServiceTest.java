package com.swoo.fitlog.api.domain.auth.service;

import com.swoo.fitlog.api.exception.ExpiredOtpException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OtpServiceTest {

    @Autowired
    private OtpService otpService;

    @Test
    void generateAndSaveOTP() {
        // given
        String email = "abc@naver.com";

        // when
        String otp = otpService.generateAndSaveOTP(email);

        // then
        Assertions.assertTrue(otpService.verifyOTP(email, otp));
    }

    @Test
    void NoExistOtp() {
        // given
        String email = "abc@gmail.com";

        // when
        // then
        Assertions.assertThrows(ExpiredOtpException.class, () -> otpService.verifyOTP(email, "123456"));
    }
}