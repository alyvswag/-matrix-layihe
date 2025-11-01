package org.example.demo13213.exception;







import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.demo13213.exception.types.NotFoundExceptionType;
import org.example.demo13213.exception.types.NullNotAllowedExceptionType;
import org.example.demo13213.model.dto.response.base.ResponseMessages;

import java.util.Map;

import static org.example.demo13213.model.dto.enums.response.ErrorResponseMessages.*;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class BaseException extends RuntimeException {
    ResponseMessages responseMessages;
    NotFoundExceptionType notFoundData;
    NullNotAllowedExceptionType nullNotAllowedData;

    @Override
    public String getMessage() {
        return responseMessages.message();
    }

    public static BaseException of(ResponseMessages responseMessage) {
        return BaseException.builder()
                .responseMessages(responseMessage)
                .build();
    }

    public static BaseException unexpected() {
        return of(UNEXPECTED);
    }

    public static BaseException notFound(String target, String field, Object value) {
        return BaseException.builder()
                .responseMessages(NOT_FOUND)
                .notFoundData(
                        NotFoundExceptionType.of(target, Map.of(field, value))
                )
                .build();
    }
    public static BaseException nullNotAllowed(String target) {
        return BaseException.builder()
                .responseMessages(NULL_NOT_ALLOWED)
                .nullNotAllowedData(
                        NullNotAllowedExceptionType.of(target)
                )
                .build();
    }


}
