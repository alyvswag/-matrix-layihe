package org.example.demo13213.controller.file;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.demo13213.model.dto.request.file.UrlRequestData;
import org.example.demo13213.model.dto.request.login.LoginRequestPayload;
import org.example.demo13213.model.dto.response.base.BaseResponse;
import org.example.demo13213.model.dto.response.file.JobsResponse;
import org.example.demo13213.model.dto.response.login.LoginResponse;
import org.example.demo13213.service.job.JobService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/jobs")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class JobController {

    final JobService jobService;

    @GetMapping("/report-jobs")
    public BaseResponse<JobsResponse> reportJobs(@RequestParam Long id) {
        return BaseResponse.success(jobService.reportJobs(id));
    }
    @PostMapping(value = "/request-url", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<JobsResponse> request(
            @RequestPart(required = false) UrlRequestData requestData, // JSON hissə
            @RequestPart(required = false) MultipartFile file          // Fayl hissə
    ) {
        return BaseResponse.success(jobService.request(requestData, file));
    }

}
