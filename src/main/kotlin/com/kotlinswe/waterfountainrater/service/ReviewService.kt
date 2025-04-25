package com.kotlinswe.waterfountainrater.service

import com.kotlinswe.waterfountainrater.dto.review.WaterFountainReviewDto
import com.kotlinswe.waterfountainrater.repository.*
import com.kotlinswe.waterfountainrater.model.WaterFountain
import com.kotlinswe.waterfountainrater.model.WaterFountainReview
import jakarta.transaction.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class ReviewService(
    private val fountainRepository: WaterFountainRepository,
    private val reviewRepository: WaterFountainReviewRepository
) {
    @Transactional
    suspend fun addReview(reviewDto: WaterFountainReviewDto
    ): WaterFountainReview {
        val fountain = fountainRepository.findById(reviewDto.fountainId)
            .orElseThrow { IllegalArgumentException("Fountain not found") }

        val review = WaterFountainReview(
            tasteRating = reviewDto.taste,
            flowRating = reviewDto.flow,
            temperatureRating = reviewDto.temperature,
            ambienceRating = reviewDto.ambience,
            usabilityRating = reviewDto.usability,
            review = reviewDto.review,
            waterFountain = fountain
        )

        return reviewRepository.save(review)
    }

    suspend fun getTopRatedFountains(limit: Int = 10): List<WaterFountain> =
        fountainRepository.findTopRated(PageRequest.of(0, limit)).content

    suspend fun getReviews(fountainId: Long): List<WaterFountainReview> =
        reviewRepository.findAllByWaterFountainId(fountainId)
}
