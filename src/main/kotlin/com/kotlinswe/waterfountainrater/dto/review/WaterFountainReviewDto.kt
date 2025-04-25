package com.kotlinswe.waterfountainrater.dto.review

import com.kotlinswe.waterfountainrater.model.WaterFountainReview

data class WaterFountainReviewDto(
    val fountainId: Long,
    val taste: Double,
    val flow: Double,
    val temperature: Double,
    val ambience: Double,
    val usability: Double,
    val review: String
) {
    companion object {
        fun from(entity: WaterFountainReview): WaterFountainReviewDto =
            WaterFountainReviewDto(
                fountainId = entity.waterFountain.id,
                taste = entity.tasteRating,
                flow = entity.flowRating,
                temperature = entity.temperatureRating,
                ambience = entity.ambienceRating,
                usability = entity.usabilityRating,
                review = entity.review
            )
    }
}