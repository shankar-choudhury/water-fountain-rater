package com.kotlinswe.waterfountainrater.controller

import com.kotlinswe.waterfountainrater.dto.review.WaterFountainReviewDto
import com.kotlinswe.waterfountainrater.dto.waterfountain.WaterFountainDto
import com.kotlinswe.waterfountainrater.model.WaterFountain
import com.kotlinswe.waterfountainrater.model.WaterFountainReview
import com.kotlinswe.waterfountainrater.service.ReviewService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/reviews")
class ReviewController(
    private val reviewService: ReviewService
) {
    @PostMapping("/{add-review}")
    suspend fun addReview(
        @RequestBody reviewDto: WaterFountainReviewDto
    ): ResponseEntity<WaterFountainReview> {
        val review = reviewService.addReview(reviewDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(review)
    }

    @GetMapping("/{fountainId}")
    suspend fun getReviews(@PathVariable fountainId: Long): List<WaterFountainReview> =
        reviewService.getReviewsByWaterFountain(fountainId)

    @GetMapping("/top-rated/{topX}")
    suspend fun getTopRated(@PathVariable topX: Int): List<WaterFountainDto> =
        reviewService.getTopRatedFountains(topX)
}