package com.kotlinswe.waterfountainrater.dto.building

data class BuildingSearchRequestDto(
    val latitude: Double,
    val longitude: Double,
    val radius: Double
)
