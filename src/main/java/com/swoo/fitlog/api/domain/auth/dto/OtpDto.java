package com.swoo.fitlog.api.domain.auth.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Setter @Getter
public class OtpDto {

    @NotBlank
    @Length(min = 6, max = 6)
    private String otp;

    @NotBlank
    @Email
    private String email;
}
