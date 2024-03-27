package com.swoo.fitlog.api.domain.auth;

import com.swoo.fitlog.api.domain.auth.dto.EmailDto;
import com.swoo.fitlog.api.domain.auth.dto.OtpDto;
import com.swoo.fitlog.api.domain.auth.service.MailSendService;
import com.swoo.fitlog.api.domain.auth.service.OtpService;
import com.swoo.fitlog.api.domain.auth.service.PasswordAuthService;
import com.swoo.fitlog.api.domain.user.dto.UserDto;
import com.swoo.fitlog.api.exception.ExpiredOtpException;
import com.swoo.fitlog.api.http.ErrorResponse;
import com.swoo.fitlog.api.http.RestResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final MailSendService mailSendService;
    private final OtpService otpService;
    private final PasswordAuthService passwordAuthService;

    /*
        이메일 수신 후 인증 번호 발송
     */
    @PostMapping("/api/v1/auth/locals/email")
    public ResponseEntity<Object> receiveEmail(@Valid @RequestBody EmailDto emailDto) throws MessagingException {

        String email = emailDto.getEmail();
        log.debug("[E-mail]=[{}]", email);

        mailSendService.send(email);

        RestResponse<Object> restResponse = RestResponse.builder()
                .code(200)
                .httpStatus(HttpStatus.OK)
                .message("이메일 인증 번호 발송")
                .build();

        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }

    @GetMapping("/api/v1/auth/locals/email/expiration")
    public ResponseEntity<Object> sendExpirationTime(@RequestParam("email") String email) {
        Long expiration = otpService.getExpiration(email);

        RestResponse<Object> restResponse = RestResponse.builder()
                .code(200)
                .httpStatus(HttpStatus.OK)
                .message("유효 시간 획득 성공")
                .data(expiration)
                .build();

        return new ResponseEntity<>(restResponse, HttpStatus.OK);
    }

    @PostMapping("/api/v1/auth/locals/email/otp")
    public ResponseEntity<Object> certifyOtp(@RequestBody @Valid OtpDto otpDto) {
        String otp = otpDto.getOtp();
        String email = otpDto.getEmail();

        try {
            // 일치하지 않는 인증 번호
            if (!otpService.verifyOTP(email, otp)) {
                ErrorResponse errorResponse = ErrorResponse
                        .of(400, HttpStatus.BAD_REQUEST, "예기치 않은 오류가 발생했습니다.", List.of(70));

                return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
            }

        } catch (ExpiredOtpException e) {
            // 만료된 인증 번호
            ErrorResponse errorResponse = ErrorResponse
                    .of(400, HttpStatus.BAD_REQUEST, "예기치 않은 오류가 발생했습니다.", List.of(71));

            return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
        }

        // 인증 성공
        RestResponse<Object> restResponse = RestResponse
                .of(200, HttpStatus.OK, "이메일 인증 성공", null);

        return new ResponseEntity<>(restResponse, restResponse.getHttpStatus());
    }

    @PostMapping("/api/v1/auth/locals/password")
    public ResponseEntity<Object> verifyPassword(@RequestBody @Valid UserDto passwordDto) {
        String email = passwordDto.getEmail();
        String password = passwordDto.getPassword();

        passwordAuthService.verifyPassword(email, password);

        RestResponse<Object> restResponse =
                RestResponse.of(200, HttpStatus.OK, "비밀 번호 검증 성공", null);
        return new ResponseEntity<>(restResponse, restResponse.getHttpStatus());
    }
}
