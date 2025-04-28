package com.kotlinswe.waterfountainrater.cli.formatter

import com.kotlinswe.waterfountainrater.dto.waterstation.WaterStationDto

fun formatAll(station: WaterStationDto): String {
    return "🚰 Station ${station.id}: Building ${station.buildingId} Floor ${station.floor}, ${station.description}"
}

fun formatById(station: WaterStationDto): String {
    return "🚰 Station ${station.id}: Floor ${station.floor}, ${station.description}"
}