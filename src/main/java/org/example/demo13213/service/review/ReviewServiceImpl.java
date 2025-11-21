package org.example.demo13213.service.review;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.demo13213.exception.BaseException;
import org.example.demo13213.model.dao.Products;
import org.example.demo13213.model.dao.Reviews;
import org.example.demo13213.model.dao.Users;
import org.example.demo13213.model.dto.request.review.ReviewRequestCreate;
import org.example.demo13213.model.dto.request.review.ReviewRequestUpdate;
import org.example.demo13213.repo.product.ProductRepo;
import org.example.demo13213.repo.review.ReviewRepo;
import org.example.demo13213.repo.user.UserRepo;
import org.example.demo13213.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    final UserRepo userRepo;
    final ProductRepo productRepo;
    final ReviewRepo reviewRepo;


    @Override
    public void addReview(ReviewRequestCreate requestCreate) {
        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Users u = userRepo.findUserByUsername(user.getUsername())
                .orElseThrow(() -> {
                    log.error("❌ User not found: username={}", user.getUsername());
                    return BaseException.notFound(Users.class.getSimpleName(), "username", user.getUsername());
                });

        Products product = productRepo.findByIdForProduct(requestCreate.getProductId()).orElseThrow(() -> {
            log.error("Product not found with id={}", requestCreate.getProductId());
            return BaseException.notFound("product", requestCreate.getProductId().toString(), requestCreate.getProductId());
        });

        Reviews r = new Reviews();
        r.setUser(u);
        r.setProduct(product);
        r.setRating(requestCreate.getRating());
        r.setComment(requestCreate.getComment());
        reviewRepo.save(r);
    }

    @Override
    public List<Reviews> getReviews(Long productId) {

        return List.of();
    }

    @Override
    public void updateRatingAndReview(ReviewRequestUpdate ratAndRevRequestUpdate) {

    }

    @Override
    public void deleteRatingAndReview(Long id) {

    }
}
