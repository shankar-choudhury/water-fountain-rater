package com.kotlinswe.waterfountainrater.repository

import com.kotlinswe.waterfountainrater.model.WaterFountainReview
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface WaterFountainReviewRepository: JpaRepository<WaterFountainReview, Long> {
    fun findAllByWaterFountainId(fountainId: Long): List<WaterFountainReview>

    fun countByWaterFountainId(fountainId: Long): Long

    @Query("""
        SELECT AVG(
            (r.tasteRating + r.flowRating + r.temperatureRating + 
             r.ambienceRating + r.usabilityRating)/5.0
        ) 
        FROM WaterFountainReview r
        WHERE r.waterFountain.id = :fountainId
    """)
    fun averageRatingForFountain(fountainId: Long): Double?

    @Query("SELECT COUNT(DISTINCT r.waterFountain) FROM WaterFountainReview r")
    fun countDistinctFountainsWithReviews(): Long
}