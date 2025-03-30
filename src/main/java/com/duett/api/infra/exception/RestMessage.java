package com.duett.api.infra.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RestMessage {
    private Integer code;
    private String message;
}
