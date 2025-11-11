package org.example.demo13213.model.dto.response.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponseDetails {
    Long id ;
    String name ;
    String description ;
    Integer finalPrice;
}
