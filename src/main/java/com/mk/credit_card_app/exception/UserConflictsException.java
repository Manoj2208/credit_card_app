package com.mk.credit_card_app.exception;

import com.mk.credit_card_app.util.ErrorConstant;

public class UserConflictsException extends GlobalException {
    public UserConflictsException() {
        super(ErrorConstant.RESOURCE_CONFLICT_MESSAGE, ErrorConstant.RESOURCE_CONFLICT_CODE);
    }
}
