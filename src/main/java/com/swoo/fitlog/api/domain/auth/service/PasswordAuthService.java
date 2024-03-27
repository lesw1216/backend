package com.swoo.fitlog.api.domain.auth.service;

import com.swoo.fitlog.api.exception.ExpiredOtpException;
import com.swoo.fitlog.api.exception.ExpiredPasswordException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordAuthService {

    private static final Long EXPIRATION_TIME_MINUTES = 5L; // 유효 시간 5분

    private final RedisTemplate<String, String> redisTemplate;

    /*
    * 회원 가입을 위해 입력한 비밀 번호를 임시로 redis에 저장하는 메서드
    *
    * 1. key 생성
    * 2. redis에 비밀 번호 저장, 유효 시간은 5분으로 설정
    * */
    public void verifyPassword(String email, String password) {
        String key = generateKey(email);

        redisTemplate.opsForValue().set(key, password, EXPIRATION_TIME_MINUTES, TimeUnit.MINUTES);
    }

    /*
    * 회원 가입을 위해 임시로 저장된 비밀 번호와 재 입력한 비밀 번호가 일치하는 지 확인 하는 메서드
    *
    * 1. key 생성
    * 2. redis에서 key를 이용해 비밀 번호 조회
    * 3. 비밀 번호가 존재하지 않을 경우 ExpiredPasswordException 예외 던지기
    * 4. 저장된 임시 비밀 번호가 재 확인 비밀 번호와 같은지 비교 후 결과 값을 반환
    * */
    public boolean certifyPassword(String email, String rePassword) {
        String key = generateKey(email);

        String savedPassword = redisTemplate.opsForValue().getAndDelete(key);

        if (savedPassword == null) {
            throw new ExpiredPasswordException("만료된 비밀 번호");
        }

        return savedPassword.equals(rePassword);
    }

    private String generateKey(String email) {
        return "PWD:" + email;
    }
}
