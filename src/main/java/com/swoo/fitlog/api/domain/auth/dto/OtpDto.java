package com.swoo.fitlog.api.domain.auth.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Setter @Getter
public class OtpDto {

    @Min(value = 100000)
    @Max(value = 999999)
    private int otp;

    @NotBlank
    @Email
    private String email;
}
