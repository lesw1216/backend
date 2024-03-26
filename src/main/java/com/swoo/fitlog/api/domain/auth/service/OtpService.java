package com.swoo.fitlog.api.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {

    private static final Long EXPIRATION_TIME_MINUTES = 5L;

    private final RedisTemplate<String, String> redisTemplate;

    public String generateAndSaveOTP(String email) {
        String otp = generateOTP();

        String key = "OTP:" + email;
        redisTemplate.opsForValue().set(key, otp, EXPIRATION_TIME_MINUTES, TimeUnit.MINUTES);

        log.debug("[{}]", redisTemplate.opsForValue().get(key));
        return otp;
    }

    public boolean verifyOTP(String email, String otp) {
        String key = "OTP:" + email;
        String savedOTP = redisTemplate.opsForValue().get(key);

        return otp.equals(savedOTP);
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
