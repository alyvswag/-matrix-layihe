package org.example.demo13213.model.dto.response.base;

import org.springframework.http.HttpStatus;

public interface ResponseMessages {
    String key();

    String message();

    HttpStatus httpStatus();
}