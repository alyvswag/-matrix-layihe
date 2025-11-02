package org.example.demo13213.model.dto.request.file;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UrlRequestData {
    String url;
    String filePass; // lazim olarsa
    //yuxaeridaki iki tipnden biri null ola biler
}

