package com.kotlinswe.waterfountainrater.service

import com.kotlinswe.waterfountainrater.dto.review.WaterFountainReviewDto
import com.kotlinswe.waterfountainrater.dto.waterfountain.WaterFountainDto
import com.kotlinswe.waterfountainrater.repository.*
import com.kotlinswe.waterfountainrater.model.WaterFountain
import com.kotlinswe.waterfountainrater.model.WaterFountainReview
import com.kotlinswe.waterfountainrater.util.DtoMappers.toDto
import jakarta.transaction.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class ReviewService(
    private val fountainRepository: WaterFountainRepository,
    private val reviewRepository: WaterFountainReviewRepository,
    private val reportRepository: WaterFountainReportRepository
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

    suspend fun getStats(): Map<String, Any?> {
        val totalFountains = fountainRepository.count()
        val totalReviews = reviewRepository.count()

        return mapOf(
            "totalFountains" to totalFountains,
            "totalReviews" to totalReviews,
            "brokenFountains" to reportRepository.countByStatus(WaterFountain.FountainStatus.BROKEN),
            "fountainsWithBrokenReports" to reportRepository.countFountainsWithBrokenReports(),
            "topRatedFountain" to fountainRepository.findTopRated(PageRequest.of(0, 1))
                .content.firstOrNull()?.let { toDto(it) },
            "worstRatedFountain" to fountainRepository.findWorstRated(PageRequest.of(0, 1))
                .content.firstOrNull()?.let { toDto(it) },
            "avgReviewsPerFountain" to if (totalFountains > 0) totalReviews.toDouble() / totalFountains else 0.0,
            "fountainsWithoutReviews" to fountainRepository.count() - reviewRepository.countDistinctFountainsWithReviews()
        )
    }

    @Transactional
    suspend fun getTopRatedFountains(limit: Int = 10): List<WaterFountainDto> =
        fountainRepository.findTopRated(PageRequest.of(0, limit)).content.map { toDto(it) }

    suspend fun getFountainRatingStats(fountainId: Long): Map<String, Any> {
        return mapOf(
            "totalReviews" to reviewRepository.countByWaterFountainId(fountainId),
            "averageRating" to (reviewRepository.averageRatingForFountain(fountainId) ?: 0.0),
            "reports" to reportRepository.countByWaterFountainId(fountainId)
        )
    }

    suspend fun getAllReviews(): List<WaterFountainReview> {
        return reviewRepository.findAll()
    }

    suspend fun getReviewsByWaterFountain(fountainId: Long): List<WaterFountainReview> =
        reviewRepository.findAllByWaterFountainId(fountainId)
}
