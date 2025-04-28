package com.kotlinswe.waterfountainrater.cli.formatter

import com.kotlinswe.waterfountainrater.dto.building.BuildingSearchResponseDto

fun formatNearby(building: BuildingSearchResponseDto, distance: Double): String {
    return "  🏢 ${building.name} (${"%.2f".format(distance)}m away) - ID: ${building.id}"
}