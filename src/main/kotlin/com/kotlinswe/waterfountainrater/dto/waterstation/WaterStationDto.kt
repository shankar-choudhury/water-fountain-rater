package com.kotlinswe.waterfountainrater.dto.waterstation

import com.kotlinswe.waterfountainrater.dto.waterfountain.WaterFountainDto

data class WaterStationDto(
    val id: Long,
    val floor: Int,
    val description: String,
    val buildingId: Long,
    val fountains: List<WaterFountainDto>
)