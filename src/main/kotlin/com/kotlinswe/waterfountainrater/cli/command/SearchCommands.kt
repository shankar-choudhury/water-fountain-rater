package com.kotlinswe.waterfountainrater.cli.command

import com.kotlinswe.waterfountainrater.service.SearchService
import com.kotlinswe.waterfountainrater.cli.util.searchEntities
import com.kotlinswe.waterfountainrater.cli.formatter.formatNearby
import com.kotlinswe.waterfountainrater.dto.building.BuildingSearchResponseDto
import com.kotlinswe.waterfountainrater.util.DistanceCalculator
import kotlinx.coroutines.coroutineScope

class SearchCommands(
    private val searchService: SearchService
) {
    suspend fun searchFountains(args: String) = coroutineScope {
        searchEntities(
            query = args,
            searchFunction = { searchService.searchFountains(it) },
            formatResult = { fountain ->
                "  ${fountain.id}. ${fountain.type} (★ ${"%.1f".format(fountain.overallRating)})\n" +
                        "     Status: ${fountain.status}"
            },
            usageMessage = "Usage: search [query]",
            emptyMessage = { query -> "🔍 No fountains matching '$query'" }
        )
    }

    suspend fun findNearby(args: String) = coroutineScope {
        findNearbyBuildings(
            args = args,
            fetchBuildings = { lat, lon, radius -> searchService.findBuildingsNear(lat, lon, radius * 1.1) },
            title = "Nearby Buildings"
        )
    }

    suspend fun findNearbyWithFountains(args: String) = coroutineScope {
        findNearbyBuildings(
            args = args,
            fetchBuildings = { lat, lon, radius -> searchService.findNearbyBuildingsWithFountains(lat, lon, radius) },
            title = "Nearby Buildings With Fountains",
            showExtraInfo = { building -> "Stations: ${building.waterStations.size}" }
        )
    }

    private suspend fun findNearbyBuildings(
        args: String,
        fetchBuildings: suspend (latitude: Double, longitude: Double, radius: Double) -> List<BuildingSearchResponseDto>,
        title: String,
        showExtraInfo: (BuildingSearchResponseDto) -> String = { "" }
    ) = coroutineScope {
        val parts = args.split("\\s+".toRegex())
        if (parts.size < 2) {
            println("Usage: [latitude] [longitude]")
            return@coroutineScope
        }

        try {
            val latitude = parts[0].toDouble()
            val longitude = parts[1].toDouble()
            val radius = 100.0

            val buildings = fetchBuildings(latitude, longitude, radius)
                .filter { building ->
                    DistanceCalculator.calculate(latitude, longitude, building.latitude, building.longitude) <= radius
                }

            if (buildings.isEmpty()) {
                println("No buildings found within $radius m.")
                return@coroutineScope
            }

            println("\n📍 $title (within $radius m):")
            buildings.forEach { building ->
                val distance = DistanceCalculator.calculate(latitude, longitude, building.latitude, building.longitude)
                println(formatNearby(building, distance))
                val extraInfo = showExtraInfo(building)
                if (extraInfo.isNotBlank()) {
                    println("    $extraInfo")
                }
            }
        } catch (e: Exception) {
            println("❌ Error finding nearby buildings: ${e.message}")
        }
    }
}