package com.kotlinswe.waterfountainrater.dto.building

import com.kotlinswe.waterfountainrater.dto.waterstation.WaterStationDto

data class BuildingSearchResponseDto(
    val id: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val waterStations: List<WaterStationDto>
)
