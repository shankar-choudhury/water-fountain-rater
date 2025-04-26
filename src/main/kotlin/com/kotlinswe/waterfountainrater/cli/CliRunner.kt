package com.kotlinswe.waterfountainrater.cli

import com.kotlinswe.waterfountainrater.dto.review.WaterFountainReviewDto
import com.kotlinswe.waterfountainrater.service.*
import com.kotlinswe.waterfountainrater.util.DistanceCalculator
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.*
import kotlinx.coroutines.runBlocking

@Component
class CliRunner(
    private val buildingService: BuildingService,
    private val stationService: WaterStationService,
    private val fountainService: WaterFountainService,
    private val reviewService: ReviewService,
    private val searchService: SearchService
) : CommandLineRunner {

    private val scanner = Scanner(System.`in`)

    override fun run(vararg args: String?) {
        println("\n🚰 Water Fountain Rater CLI")
        println("Type 'help' for commands, 'exit' to quit\n")

        runBlocking {
            while (true) {
                try {
                    print("> ")
                    val input = scanner.nextLine().trim()
                    val (command, args) = parseInput(input)

                    when (command.lowercase()) {
                        "exit" -> break
                        "help" -> printHelp()
                        "list" -> buildingService.listAll()
                        "stations" -> stationService.listAll()
                        "fountains" -> fountainService.listAll()
                        "rate" -> rateFountain(args)
                        "top" -> showTopRated(args)
                        "near" -> findNearby(args)
                        "details" -> fountainService.showDetails(args.toLongOrNull() ?: -1)
                        else -> println("❌ Unknown command. Type 'help' for options.")
                    }
                } catch (e: Exception) {
                    println("⚠️ Error: ${e.message}")
                }
            }
        }
    }

    private fun parseInput(input: String): Pair<String, String> {
        val parts = input.split(" ", limit = 2)
        return if (parts.size > 1) parts[0] to parts[1] else parts[0] to ""
    }

    private fun printHelp() {
        println(
            """
            📋 Available Commands:

            BUILDINGS:
              list               - List all buildings
              near [lat] [lon]   - Find buildings near location

            STATIONS:
              stations [buildingId] - List stations in a building

            FOUNTAINS:
              fountains [stationId] - List fountains in a station
              rate [id] [taste] [flow] [temp] [amb] [usability] - Rate a fountain
              top [limit]        - Show top-rated fountains (default: 5)
              details [id]       - Show fountain details

            SYSTEM:
              help               - Show this help
              exit               - Exit the application
        """.trimIndent()
        )
    }

    private suspend fun rateFountain(args: String) {
        val parts = args.split("\\s+".toRegex())
        if (parts.size < 6) {
            println("Usage: rate [id] [taste] [flow] [temp] [amb] [usability]")
            return
        }

        try {
            val fountainId = parts[0].toLong()
            val taste = parts[1].toDouble()
            val flow = parts[2].toDouble()
            val temp = parts[3].toDouble()
            val amb = parts[4].toDouble()
            val usability = parts[5].toDouble()

            print("Enter a short review: ")
            val reviewText = scanner.nextLine().trim()

            val reviewDto = WaterFountainReviewDto(
                fountainId = fountainId,
                taste = taste,
                flow = flow,
                temperature = temp,
                ambience = amb,
                usability = usability,
                review = reviewText)

            val review = reviewService.addReview(
                reviewDto
            )

            println("\n✅ Submitted review for fountain ${review.waterFountain.id}")
            println("   Taste: ${review.tasteRating}, Flow: ${review.flowRating}, Temp: ${review.temperatureRating}, Amb: ${review.ambienceRating}, Usability: ${review.usabilityRating}")
        } catch (e: Exception) {
            println("❌ Error submitting review: ${e.message}")
        }
    }

    private suspend fun showTopRated(args: String) {
        val limit = args.toIntOrNull() ?: 5

        val fountains = reviewService.getTopRatedFountains(limit)
        if (fountains.isEmpty()) {
            println("No fountains found.")
            return
        }

        println("\n🏆 Top $limit Fountains:")
        for ((index, fountain) in fountains.withIndex()) {
            val station = fountain.station
            val building = station.building
            println("  ${index + 1}. ★ ${"%.2f".format(fountain.overallRating)} - ${fountain.type} | ${building.name} (Floor ${station.floor})")
            println("     Station: ${station.description}")
        }
    }

    private suspend fun findNearby(args: String) {
        val parts = args.split("\\s+".toRegex())
        if (parts.size < 2) {
            println("Usage: near [latitude] [longitude]")
            return
        }

        try {
            val latitude = parts[0].toDouble()
            val longitude = parts[1].toDouble()
            val radius = 100.0

            // Step 1: Fetch buildings within a rough bounding box (larger radius)
            val buildings = searchService.findBuildingsNear(latitude, longitude, radius * 1.1) // Expanding radius a bit to cover edge cases

            if (buildings.isEmpty()) {
                println("No buildings found within $radius m.")
                return
            }

            // Step 2: Filter buildings using the Haversine formula to refine the result
            val nearbyBuildings = buildings.filter { building ->
                val distance = DistanceCalculator.calculate(latitude, longitude, building.latitude, building.longitude)
                distance <= radius
            }

            if (nearbyBuildings.isEmpty()) {
                println("No buildings found within $radius m.")
                return
            }

            println("\n📍 Nearby Buildings (within $radius m):")
            nearbyBuildings.forEach { building ->
                val distance = DistanceCalculator.calculate(latitude, longitude, building.latitude, building.longitude)
                println("  🏢 ${building.name} (${"%.2f".format(distance)}m away) - ID: ${building.id}")
            }
        } catch (e: Exception) {
            println("❌ Error finding nearby buildings: ${e.message}")
        }
    }
}