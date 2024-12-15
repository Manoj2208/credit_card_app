package com.mk.credit_card_app.exception;

import com.mk.credit_card_app.util.ErrorConstant;

public class UnauthorizedException extends GlobalException{
    public UnauthorizedException() {
        super(ErrorConstant.UNAUTHORIZED_MESSAGE, ErrorConstant.UNAUTHORIZED_CODE);
    }
}
