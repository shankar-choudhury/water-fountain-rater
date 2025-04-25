package com.kotlinswe.waterfountainrater.dto.building

import com.kotlinswe.waterfountainrater.dto.waterstation.WaterStationDto
import com.kotlinswe.waterfountainrater.model.Building

data class BuildingSearchResponseDto(
    val id: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val waterStations: List<WaterStationDto>
) {
    companion object {
        fun from(entity: Building): BuildingSearchResponseDto = BuildingSearchResponseDto(
            id = entity.id,
            name = entity.name,
            latitude = entity.latitude,
            longitude = entity.longitude,
            waterStations = entity.waterStations.map { WaterStationDto.from(it) }
        )
    }
}
