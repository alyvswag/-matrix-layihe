package org.example.demo13213.service.job;

import org.example.demo13213.model.dto.request.file.UrlRequestData;
import org.example.demo13213.model.dto.response.file.JobsResponse;
import org.springframework.web.multipart.MultipartFile;

public interface JobService {
    JobsResponse downloadFromUrlAndAnalysis(UrlRequestData urlRequestData);
    JobsResponse reportJobs(Long id);

    JobsResponse request(UrlRequestData requestData, MultipartFile file);

}
