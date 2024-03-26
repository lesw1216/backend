package com.swoo.fitlog.api.domain.auth;

import com.swoo.fitlog.api.domain.auth.dto.EmailDto;
import com.swoo.fitlog.api.domain.auth.service.MailSendService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final MailSendService mailSendService;

    /*
        이메일 수신 후 인증 번호 발송
     */
    @PostMapping("/api/v1/auth/locals/email")

    public String receiveEmail(@Valid @RequestBody EmailDto emailDto) throws MessagingException {
        String email = emailDto.getEmail();
        log.debug("[E-mail]=[{}]", email);

        mailSendService.send(email);

        return "success";
    }
}
