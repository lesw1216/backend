package com.swoo.fitlog.api.domain.auth.service;

import com.swoo.fitlog.api.exception.ExpiredOtpException;
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

    private static final Long EXPIRATION_TIME_MINUTES = 5L; // 유효 시간 5분

    private final RedisTemplate<String, String> redisTemplate;

    /*
    * 인증 번호 생성하고 저장
    *
    * 1. 인증 번호 생성 메서드 호출
    * 2. email과 "OTP:" 문자열을 조합하여 KEY 생성
    * 3. redis에 KEY:OTP 저장
    * 4. 인증 번호 반환
    * */
    public String generateAndSaveOTP(String email) {
        String otp = generateOTP();
        String key = createKey(email);

        redisTemplate.opsForValue().set(key, otp, EXPIRATION_TIME_MINUTES, TimeUnit.MINUTES);

        log.debug("[KEY][{}]", redisTemplate.opsForValue().get(key));
        return otp;
    }

    /*
    * 인증 번호 유효성 검사
    *
    * 1. email과 "OTP:" 문자열 조합으로 key 생성
    * 2. redis에 해당 키로 인증 번호 가져오고 삭제
    * 3. 인증 번호가 없는 경우(null) ExpiredOtpException 예외 발생
    * 4. 사용자가 입력한 인증 번호와 조회한 인증 번호 비교
    * */
    public boolean verifyOTP(String email, String otp) {
        String key = createKey(email);
        String savedOTP = redisTemplate.opsForValue().getAndDelete(key);

        /*
        * 현재 email에 대한 인증 번호가 존재하지 않았을 경우
        * */
        if (savedOTP == null) {
            throw new ExpiredOtpException("만료된 인증 번호");
        }

        return otp.equals(savedOTP);
    }

    /*
    * 유효 시간 조회
    * */
    public Long getExpiration(String email) {
        String key = createKey(email);

        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /*
    * key 생성
    * */
    private String createKey(String email) {
        return "OTP:" + email;
    }

    /*
    * 인증 번호 생성
    * */
    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);  // 100000 ~ 999999 사이의 무작위 정수
        return String.valueOf(otp);
    }
}
