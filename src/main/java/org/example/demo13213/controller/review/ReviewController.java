package org.example.demo13213.controller.review;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.demo13213.model.dao.Reviews;
import org.example.demo13213.model.dto.request.review.ReviewRequestCreate;
import org.example.demo13213.model.dto.request.review.ReviewRequestUpdate;
import org.example.demo13213.model.dto.response.base.BaseResponse;
import org.example.demo13213.service.review.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewController {

    final ReviewService reviewService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add-review")
    public BaseResponse<Reviews> addReview(@RequestBody ReviewRequestCreate ratingAndReview) {
        return BaseResponse.created(reviewService.addReview(ratingAndReview));
    }

    @GetMapping("/get-reviews/{productId}")
    public BaseResponse<List<Reviews>> getReviews(@PathVariable Long productId) {
        return BaseResponse.success(reviewService.getReviews(productId));
    }

    @PostMapping("/update-review/")
    public BaseResponse<Void> updateReview(@RequestBody ReviewRequestUpdate requestUpdate) {
        reviewService.updateReview(requestUpdate);
        return BaseResponse.success();
    }

    @DeleteMapping("/delete-review/{productId}")
    public BaseResponse<Void> deleteReview(@PathVariable Long productId) {
        reviewService.deleteReview(productId);
        return BaseResponse.success();
    }
}