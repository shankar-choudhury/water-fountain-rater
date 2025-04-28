package com.kotlinswe.waterfountainrater.cli.formatter

import com.kotlinswe.waterfountainrater.dto.waterfountain.WaterFountainDto

fun formatAll(fountain: WaterFountainDto): String {
    return "💧 Fountain ${fountain.id}: Station ${fountain.stationId} Type ${fountain.type}, ★ ${"%.1f".format(fountain.overallRating)}"
}

fun formatById(fountain: WaterFountainDto): String {
    return "💧 Fountain ${fountain.id}: Type ${fountain.type}, ★ ${"%.1f".format(fountain.overallRating)}"
}

fun formatDetails(fountain: WaterFountainDto): String {
    return """
            💧 Fountain ${fountain.id} Details:
               Type: ${fountain.type}
               Status: ${fountain.status}
               Overall Rating: ★ ${"%.2f".format(fountain.overallRating)}
               Station: ${fountain.stationId}
        """.trimIndent()
}

fun formatTopRated(index: Int, fountain: WaterFountainDto): String {
    return "  ${index + 1}. ★ ${"%.2f".format(fountain.overallRating)} - ${fountain.type}"
}