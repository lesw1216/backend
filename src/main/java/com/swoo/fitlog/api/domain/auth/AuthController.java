package com.swoo.fitlog.api.domain.auth;

import com.swoo.fitlog.api.domain.auth.dto.EmailDto;
import com.swoo.fitlog.api.domain.auth.service.MailSendService;
import com.swoo.fitlog.api.domain.auth.service.OtpService;
import com.swoo.fitlog.api.http.RestApiResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final MailSendService mailSendService;
    private final OtpService otpService;

    /*
        이메일 수신 후 인증 번호 발송
     */
    @PostMapping("/api/v1/auth/locals/email")
    public ResponseEntity<Object> receiveEmail(@Valid @RequestBody EmailDto emailDto) throws MessagingException {
        String email = emailDto.getEmail();
        log.debug("[E-mail]=[{}]", email);

        mailSendService.send(email);

        RestApiResponse<Object> restApiResponse = RestApiResponse.builder()
                .code(200)
                .httpStatus(HttpStatus.OK)
                .message("이메일 인증 번호 발송")
                .build();

        return new ResponseEntity<>(restApiResponse, HttpStatus.OK);
    }

    @GetMapping("/api/v1/auth/locals/email/expiration")
    public ResponseEntity<Object> sendExpirationTime(@RequestParam("email") String email) {
        Long expiration = otpService.getExpiration(email);

        RestApiResponse<Object> restApiResponse = RestApiResponse.builder()
                .code(200)
                .httpStatus(HttpStatus.OK)
                .message("유효 시간 획득 성공")
                .data(expiration)
                .build();

        return new ResponseEntity<>(restApiResponse, HttpStatus.OK);
    }
}
