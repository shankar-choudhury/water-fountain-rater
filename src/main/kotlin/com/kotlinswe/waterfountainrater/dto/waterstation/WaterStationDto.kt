package com.kotlinswe.waterfountainrater.dto.waterstation

import com.kotlinswe.waterfountainrater.dto.waterfountain.WaterFountainDto
import com.kotlinswe.waterfountainrater.model.WaterStation

data class WaterStationDto(
    val id: Long,
    val floor: Int,
    val description: String,
    val fountains: List<WaterFountainDto>
) {
    companion object {
        fun from(entity: WaterStation): WaterStationDto = WaterStationDto(
            id = entity.id,
            floor = entity.floor,
            description = entity.description,
            fountains = entity.fountains.map { WaterFountainDto.from(it) }
        )
    }
}
