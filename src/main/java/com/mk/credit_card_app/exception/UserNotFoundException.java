package com.mk.credit_card_app.exception;

import com.mk.credit_card_app.util.ErrorConstant;

public class UserNotFoundException extends GlobalException{
    public UserNotFoundException() {
        super(ErrorConstant.RESOURCE_NOT_FOUND_MSG, ErrorConstant.RESOURCE_NOT_FOUND_CODE);
    }
}
