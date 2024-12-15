package com.mk.credit_card_app.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessConstant {
    public static final String CARD_APPLIED_CODE= "2001";
    public static final String CARD_APPLIED_MSG= "Card applied successfully";
    public static final String LOGIN_SUCCESS_CODE= "2002";
    public static final String LOGIN_SUCCESS_MSG = "Log in successful";
    public static final String CARD_DETAILS_FOUND_CODE= "2003";
    public static final String CARD_DETAILS_FOUND_MESSAGE= "Card Details Fetched Successfully";
}
