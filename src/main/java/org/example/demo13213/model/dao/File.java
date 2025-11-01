package org.example.demo13213.model.dao;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_file_user"))
    Users users;

    @Column(name = "file_name")
    String fileName;

    @Column(name = "file_hash")
    String fileHash;

    @Column(name = "file_size")
    Long fileSize;

    @Column(name = "file_type")
    String fileType;

    @CreationTimestamp
    @Column(name = "uploaded_at",  updatable = false)
    Timestamp uploadedAt;

    @Column(name = "analyzed_at")
    Timestamp analyzedAt;
}

