package com.swd392.preOrderBlindBox.exception;

import com.swd392.preOrderBlindBox.enums.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final String errorCode;
    private final String message;

    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
