package org.example.demo13213.exception.types;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Data
@Builder(access = PROTECTED)
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
@FieldDefaults(level = PRIVATE)
public class NullNotAllowedExceptionType {
    String target;

    public static NullNotAllowedExceptionType of(String target) {
        return NullNotAllowedExceptionType.builder()
                .target(target)
                .build();
    }
}
