package com.swoo.fitlog.api.domain.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swoo.fitlog.api.domain.auth.dto.OtpDto;
import com.swoo.fitlog.api.domain.auth.service.OtpService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OtpService otpService;

    @Test
    @DisplayName("인증 번호 인증 성공")
    void certifyOtpV1() throws Exception {
        // given
        String URI = "/api/v1/auth/locals/email/otp";
        String email = "test@gmail.com";

        // when
        String otp = otpService.generateAndSaveOTP(email);

        OtpDto otpDto = new OtpDto();
        otpDto.setEmail(email);
        otpDto.setOtp(otp);

        String otpJson = objectMapper.writeValueAsString(otpDto);


        // then
        mvc.perform(MockMvcRequestBuilders
                        .post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(otpJson)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("유효 시간 만료")
    void certifyOtpV2() throws Exception {
        // given
        String URI = "/api/v1/auth/locals/email/otp";

        OtpDto otpDto = new OtpDto();
        otpDto.setOtp("123456");
        otpDto.setEmail("test1@gmail.com");

        String otpJson = objectMapper.writeValueAsString(otpDto);

        // when
        // then
        mvc.perform(MockMvcRequestBuilders.post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(otpJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCodes[0]").value(71))
                .andDo(print());
    }

    @Test
    @DisplayName("인증 번호 불일치")
    void certifyOtpV3() throws Exception {
        // given
        String URI = "/api/v1/auth/locals/email/otp";

        OtpDto otpDto = new OtpDto();
        otpDto.setOtp("123456");
        otpDto.setEmail("test2@gmail.com");

        String otpJson = objectMapper.writeValueAsString(otpDto);

        // when

        // 직접 service로 임의의 이메일 인증 번호 생성
        String otp = otpService.generateAndSaveOTP("test@gmail.com");

        // then
        mvc.perform(MockMvcRequestBuilders.post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(otpJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCodes[0]").value(70))
                .andDo(print());
    }

    @Test
    @DisplayName("유효성 검증 오류 - 인증 번호 문자 길이 불일치")
    void certifyOtpV4() throws Exception {
        // given
        String URI = "/api/v1/auth/locals/email/otp";

        OtpDto otpDto = new OtpDto();
        otpDto.setOtp("123");
        otpDto.setEmail("test2@gmail.com");

        String otpJson = objectMapper.writeValueAsString(otpDto);

        // when

        // 직접 service로 임의의 이메일 인증 번호 생성
        String otp = otpService.generateAndSaveOTP("test@gmail.com");

        // then
        mvc.perform(MockMvcRequestBuilders.post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(otpJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCodes[0]").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("유효성 검증 오류 - 공백 입력")
    void certifyOtpV5() throws Exception {
        // given
        String URI = "/api/v1/auth/locals/email/otp";

        OtpDto otpDto = new OtpDto();
        otpDto.setOtp("");
        otpDto.setEmail("test2@gmail.com");

        String otpJson = objectMapper.writeValueAsString(otpDto);

        // when

        // 직접 service로 임의의 이메일 인증 번호 생성
        String otp = otpService.generateAndSaveOTP("test@gmail.com");

        // then
        mvc.perform(MockMvcRequestBuilders.post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(otpJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCodes[0]").value(2))
                .andDo(print());
    }
}