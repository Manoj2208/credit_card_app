package com.mk.credit_card_app.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorConstant {
    public static final String RESOURCE_CONFLICT_CODE="4009";
    public static final String RESOURCE_CONFLICT_MESSAGE="Card request Already Applied for the user";

    public static final String RESOURCE_NOT_FOUND_CODE="4004";
    public static final String RESOURCE_NOT_FOUND_MSG="User not found";

    public static final String INVALID_CREDENTIALS_CODE="4000";
    public static final String INVALID_CREDENTIALS_MSG="Invalid credentials";

    public static final String UNAUTHORIZED_CODE="4003";
    public static final String UNAUTHORIZED_MESSAGE="Unauthorized user";

}
