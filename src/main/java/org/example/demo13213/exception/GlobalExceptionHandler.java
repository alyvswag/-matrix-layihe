package org.example.demo13213.exception;



import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.example.demo13213.model.dto.response.base.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@Slf4j
@RestControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<BaseResponse<?>> handleBaseException(BaseException  ex) {
        return ResponseEntity.status(ex.getResponseMessages().httpStatus()).body(BaseResponse.error(ex));
    }
    @ExceptionHandler
    public ResponseEntity<BaseResponse<?>> handleBaseException( SQLException  ex) {
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.error(ex));
    }
    @ExceptionHandler
    public ResponseEntity<BaseResponse<?>> handleBaseException(ExpiredJwtException ex) {
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.error(ex));
    }
}
//todo: expride jwt exp handle ele