package com.mk.credit_card_app.exception;

import com.mk.credit_card_app.util.ErrorConstant;

public class InvalidCredentialsException extends GlobalException{
    public InvalidCredentialsException() {
        super(ErrorConstant.INVALID_CREDENTIALS_MSG, ErrorConstant.INVALID_CREDENTIALS_CODE);
    }
}
