package com.kotlinswe.waterfountainrater.dto.waterfountain

import com.kotlinswe.waterfountainrater.model.WaterFountain

data class WaterFountainDto(
    val id: Long,
    val type: WaterFountain.FountainType,
    val status: WaterFountain.FountainStatus,
    val overallRating: Double
)
