package com.kotlinswe.waterfountainrater.dto.waterfountain

import com.kotlinswe.waterfountainrater.model.WaterFountain

data class WaterFountainDto(
    val id: Long,
    val type: WaterFountain.FountainType,
    val status: WaterFountain.FountainStatus,
    val overallRating: Double
) {
    companion object {
        fun from(entity: WaterFountain) = WaterFountainDto(
            id = entity.id,
            type = entity.type,
            status = entity.status,
            overallRating = entity.overallRating
        )
    }
}
