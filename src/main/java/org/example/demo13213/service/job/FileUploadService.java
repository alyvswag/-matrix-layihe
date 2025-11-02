package org.example.demo13213.service.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@Service
public class FileUploadService {

    // İcazə verilən fayl uzantıları
    private static final List<String> ALLOWED_EXTENSIONS = List.of(
            "exe", "dll", "ps1", "js", "docx", "xls", "ppt", "zip", "rar", "7z"
    );

    // ZIP fayllar üçün limitlər
    private static final int MAX_FILES = 500;
    private static final long MAX_TOTAL_BYTES = 500L * 1024 * 1024; // 500 MB

    public Object analyzeUploadedFile(MultipartFile file) {
        try {
            log.info("📁 Yüklənmiş fayl: {}", file.getOriginalFilename());

            if (file.isEmpty()) {
                throw new IOException("Fayl boşdur");
            }

            // 1️⃣ Müvəqqəti fayl kimi saxla
            Path tempFile = Files.createTempFile("upload_", "_" + Objects.requireNonNull(file.getOriginalFilename()));
            file.transferTo(tempFile.toFile());

            // 2️⃣ MIME və uzantı yoxlanışı
            String mimeType = Files.probeContentType(tempFile);
            String extension = getFileExtension(file.getOriginalFilename());
            log.info("📄 MIME tipi: {}, uzantı: {}", mimeType, extension);

            if (!ALLOWED_EXTENSIONS.contains(extension)) {
                throw new SecurityException("❌ Bu fayl tipi icazəli deyil: " + extension);
            }

            // 3️⃣ Hash hesablamaları
            String sha256 = calculateHash(tempFile, "SHA-256");
            String md5 = calculateHash(tempFile, "MD5");
            long fileSize = Files.size(tempFile);

            log.info("✅ SHA256: {}\n✅ MD5: {}\n✅ Ölçü: {} bayt", sha256, md5, fileSize);

            // 4️⃣ Sandbox mühiti
            Path sandboxDir = Paths.get("sandbox_storage");
            Files.createDirectories(sandboxDir);
            Path sandboxPath = sandboxDir.resolve(sha256 + "." + extension);
            Files.move(tempFile, sandboxPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("🧩 Fayl sandbox-da saxlanıldı: {}", sandboxPath.toAbsolutePath());

            // 5️⃣ Əgər ZIP fayldırsa, aç və məzmunu analiz üçün saxla
            if (extension.equals("zip")) {
                Path extractDir = sandboxDir.resolve(sha256 + "_unzipped");
                unzipSecure(sandboxPath, extractDir);
                log.info("📦 ZIP fayl açıldı: {}", extractDir.toAbsolutePath());
            } else {
                log.info("⚙️ ZIP deyil, analiz bitdi.");
            }

            log.info("✅ Fayl uğurla analiz edildi.");
            return null;

        } catch (Exception e) {
            log.error("❌ Fayl analiz xətası: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * ZIP faylını təhlükəsiz şəkildə açır (ZIP slip və ölçü limitləri ilə)
     */
    private void unzipSecure(Path zipFile, Path targetDir) throws IOException {
        Files.createDirectories(targetDir);

        long totalExtracted = 0;
        int fileCount = 0;

        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {

                Path resolved = targetDir.resolve(entry.getName()).normalize();

                // ZIP-SLIP qorunması
                if (!resolved.startsWith(targetDir)) {
                    throw new IOException("❌ Path traversal cəhdi aşkarlandı: " + entry.getName());
                }

                if (entry.isDirectory()) {
                    Files.createDirectories(resolved);
                    continue;
                }

                // limitlər
                fileCount++;
                if (fileCount > MAX_FILES) {
                    throw new IOException("❌ ZIP içində çox sayda fayl: " + fileCount);
                }

                Files.createDirectories(resolved.getParent());
                try (OutputStream os = Files.newOutputStream(resolved, StandardOpenOption.CREATE_NEW)) {
                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        os.write(buffer, 0, len);
                        totalExtracted += len;
                        if (totalExtracted > MAX_TOTAL_BYTES) {
                            throw new IOException("❌ ZIP ölçü limiti aşıldı");
                        }
                    }
                }

                zis.closeEntry();
            }
        }
    }

    /**
     * Hash hesablaması
     */
    private String calculateHash(Path file, String algorithm) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        try (InputStream fis = Files.newInputStream(file)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : digest.digest()) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Fayl uzantısını tapır
     */
    private String getFileExtension(String filename) {
        int i = filename.lastIndexOf('.');
        return (i > 0) ? filename.substring(i + 1).toLowerCase() : "";
    }
}
