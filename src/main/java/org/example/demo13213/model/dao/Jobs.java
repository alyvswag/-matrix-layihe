package org.example.demo13213.model.dao;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.demo13213.model.dto.enums.file.FileStatus;
import org.example.demo13213.model.dto.enums.file.FileType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "jobs")
public class Jobs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    Users user;

    @Column(name = "filename")
    String filename;

    @Column(name = "original_url")
    String originalUrl;

    @Column(name = "file_path")
    String filePath;

    @Column(name = "file_size_bytes")
    Long fileSizeBytes;

    @Column(name = "sha256")
    String sha256;

    @Column(name = "md5")
    String md5;

    @Column(name = "mime_type")
    String mimeType;

    @Column(name = "detected_type")
    FileType detected_type;

    @Column(name = "status")
    FileStatus status;

    @Column(name = "yara_hits", columnDefinition = "jsonb")
    String yaraHits;

    @Column(name = "static_analysis", columnDefinition = "jsonb")
    String staticAnalysis;

    @Column(name = "sandbox_result", columnDefinition = "jsonb")
    String sandboxResult;

    @Column(name = "ioc_extracted", columnDefinition = "jsonb")
    String iocExtracted;

    @Column(name = "ai_score", precision = 5, scale = 2)
    BigDecimal aiScore;

    @Column(name = "rule_score", precision = 5, scale = 2)
    BigDecimal ruleScore;

    @Column(name = "final_score", precision = 5, scale = 2)
    BigDecimal finalScore;

    @Column(name = "risk_level", length = 20)
    String riskLevel;

    @Column(name = "error_message", columnDefinition = "text")
    String errorMessage;

    @Column(name = "analysis_duration_seconds")
    Integer analysisDurationSeconds;

    @Column(name = "queue_position")
    Integer queuePosition;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    Timestamp updatedAt;
}
