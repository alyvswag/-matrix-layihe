package org.example.demo13213.security.data;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@FieldDefaults (level = AccessLevel.PRIVATE)
@ConfigurationProperties("security")
@Configuration
public class SecurityProperties {
//configurationproperties
    SecurityJwtData jwt;

}