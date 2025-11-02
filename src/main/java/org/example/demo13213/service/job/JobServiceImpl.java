package org.example.demo13213.service.job;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.example.demo13213.exception.BaseException;
import org.example.demo13213.model.dao.Jobs;
import org.example.demo13213.model.dao.Users;
import org.example.demo13213.model.dto.request.file.UrlRequestData;
import org.example.demo13213.model.dto.response.file.JobsResponse;
import org.example.demo13213.repo.jobs.JobRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    final JobRepo jobRepo;
    private final RestTemplate restTemplate = new RestTemplate();

//    @Value("${external.api.url}")
//    private String pythonApiUrl;

    @Override
    public JobsResponse downloadFromUrlAndAnalysis(UrlRequestData urlRequestData) {

        return null;
    }

    @Override
    public JobsResponse reportJobs(Long id) {
        Jobs j = findJobs(id);

        return JobsResponse.builder()
                .id(String.valueOf(j.getId()))
                .filename(j.getFilename())
                .originalUrl(j.getOriginalUrl())
                .filePath(j.getFilePath())
                .fileSizeBytes(j.getFileSizeBytes())
                .sha256(j.getSha256())
                .md5(j.getMd5())
                .mimeType(j.getMimeType())
                .detectedType(j.getDetected_type())
                .status(j.getStatus())
                .yaraHits(j.getYaraHits())
                .staticAnalysis(j.getStaticAnalysis())
                .sandboxResult(j.getSandboxResult())
                .iocExtracted(j.getIocExtracted())
                .aiScore(j.getAiScore())
                .ruleScore(j.getRuleScore())
                .finalScore(j.getFinalScore())
                .riskLevel(j.getRiskLevel())
                .errorMessage(j.getErrorMessage())
                .analysisDurationSeconds(j.getAnalysisDurationSeconds())
                .queuePosition(j.getQueuePosition())
                .createdAt(j.getCreatedAt())
                .updatedAt(j.getUpdatedAt())
                .build();
    }

    @Override
    public JobsResponse request(UrlRequestData requestData, MultipartFile file) {
        return null;
    }

//        @Override
//        public JobsResponse request(UrlRequestData requestData, MultipartFile file) {
//            try {
//
//                if (requestData != null && requestData.getUrl() != null && !requestData.getUrl().isBlank()) {
//                    log.info("URL analiz tələbi: {}", requestData.getUrl());
//                    return sendUrlToPythonApi(requestData.getUrl());
//                }
//
//
//                if (file != null && !file.isEmpty()) {
//                    String fileName = file.getOriginalFilename();
//                    String ext = getFileExtension(fileName);
//                    log.info("Fayl gəldi: {} ({} bytes)", fileName, file.getSize());
//
//                    // Arxivdirsə və şifrəlidirsə
//                    if (List.of("zip", "rar", "7z").contains(ext)) {
//                        boolean encrypted = checkIfEncrypted(file);
//
//                        if (encrypted) {
//                            if (requestData == null || requestData.getFilePass() == null) {
//                                throw new IllegalArgumentException("Şifrəli fayl üçün 'filePass' göndərilməyib.");
//                            }
//                            log.info("Şifrəli fayl parolla açılır...");
//                        }
//                    }
//
//                    // Faylı Python API-yə göndər
//                    return sendFileToPythonApi(file);
//                }
//
//                throw new IllegalArgumentException("Nə URL, nə də fayl təqdim edilib.");
//            } catch (Exception e) {
//                log.error("Xəta: {}", e.getMessage());
//                return JobsResponse.builder()
//                        .status("error")
//                        .message(e.getMessage())
//                        .build();
//            }
//        }
//
//        private String getFileExtension(String name) {
//            return name != null && name.contains(".")
//                    ? name.substring(name.lastIndexOf(".") + 1).toLowerCase()
//                    : "";
//        }
//
//        private boolean checkIfEncrypted(MultipartFile file) {
//
//            return false;
//        }
//
//        private JobsResponse sendUrlToPythonApi(String url) {
//            String endpoint = pythonApiUrl + "?type=url&value=" + url;
//            log.info("Python API çağırışı (URL): {}", endpoint);
//            return restTemplate.getForObject(endpoint, JobsResponse.class);
//        }
//
//        private JobsResponse sendFileToPythonApi(MultipartFile file) throws IOException {
//            String endpoint = pythonApiUrl + "?type=file";
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//            body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
//
//            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
//
//            ResponseEntity<JobsResponse> response = restTemplate.postForEntity(endpoint, entity, JobsResponse.class);
//            return response.getBody();
//        }
//    }


        //private
        private Jobs findJobs (Long id){
            return jobRepo.findJobsById(id)
                    .orElseThrow(
                            () -> BaseException.notFound(Jobs.class.getSimpleName(), "jobs", id)
                    );
        }
    }
