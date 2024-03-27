package com.swoo.fitlog.api.enums;

public enum ValidErrorCode {
    LENGTH(1),
    NOTBLANK(2);

    private final int code;
    
    ValidErrorCode(int code) {
        this.code = code;
    }

    public static int getErrorCode(String validName) {
        ValidErrorCode validErrorCode = ValidErrorCode.valueOf(validName.toUpperCase());

        return validErrorCode.code;
    }
}
