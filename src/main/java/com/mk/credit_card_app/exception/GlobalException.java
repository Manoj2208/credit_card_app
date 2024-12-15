package com.mk.credit_card_app.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Data
public class GlobalException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final String message;
    private final String code;
}
