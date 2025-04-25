package com.kotlinswe.waterfountainrater.repository

import com.kotlinswe.waterfountainrater.model.WaterFountainReview
import org.springframework.data.jpa.repository.JpaRepository

interface WaterFountainReviewRepository: JpaRepository<WaterFountainReview, Long> {
    fun findAllByWaterFountainId(fountainId: Long): List<WaterFountainReview>
}