package com.kotlinswe.waterfountainrater.dto.waterstation

import com.kotlinswe.waterfountainrater.dto.waterfountain.WaterFountainDto
import com.kotlinswe.waterfountainrater.model.WaterStation

data class WaterStationDto(
    val id: Long,
    val floor: Int,
    val description: String,
    val fountains: List<WaterFountainDto>
)