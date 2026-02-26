package org.example.demo13213.exception.types;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

import static lombok.AccessLevel.*;

@Data
@Builder(access = PROTECTED)
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
@FieldDefaults(level = PRIVATE)
public class NotFoundExceptionType {
    String target;
    Map<String, Object> fields;

    public static NotFoundExceptionType of(String target, Map<String, Object> fields) {
        return NotFoundExceptionType.builder()
                .fields(fields)
                .target(target)
                .build();
    }
}