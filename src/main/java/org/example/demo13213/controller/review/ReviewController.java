package org.example.demo13213.controller.review;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.demo13213.model.dao.Products;
import org.example.demo13213.model.dao.Reviews;
import org.example.demo13213.model.dto.request.review.ReviewRequestCreate;
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
    public BaseResponse<Void> addReview(@RequestBody ReviewRequestCreate ratingAndReview) {
        reviewService.addReview(ratingAndReview);
        return BaseResponse.created();
    }
    @GetMapping("/get-reviews/{productId}")
    public BaseResponse<List<Reviews>> getReviews(@PathVariable Long productId) {
        return BaseResponse.success(reviewService.getReviews(productId));
    }
}
