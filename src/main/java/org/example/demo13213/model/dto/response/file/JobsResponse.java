package org.example.demo13213.model.dto.response.file;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.demo13213.model.dao.Users;
import org.example.demo13213.model.dto.enums.file.FileStatus;
import org.example.demo13213.model.dto.enums.file.FileType;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobsResponse {
    String id;

    String filename;
    String originalUrl;
    String filePath;
    Long fileSizeBytes;

    String sha256;
    String md5;
    String mimeType;

    FileType detectedType;
    FileStatus status;

    String yaraHits;
    String staticAnalysis;
    String sandboxResult;
    String iocExtracted;

    BigDecimal aiScore;
    BigDecimal ruleScore;
    BigDecimal finalScore;

    String riskLevel;
    String errorMessage;

    Integer analysisDurationSeconds;
    Integer queuePosition;

    Timestamp createdAt;
    Timestamp updatedAt;
}
