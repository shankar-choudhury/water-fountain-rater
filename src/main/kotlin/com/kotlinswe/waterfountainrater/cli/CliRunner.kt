package com.kotlinswe.waterfountainrater.cli

import com.kotlinswe.waterfountainrater.repository.BuildingRepository
import com.kotlinswe.waterfountainrater.repository.WaterFountainRepository
import com.kotlinswe.waterfountainrater.repository.WaterStationRepository
import com.kotlinswe.waterfountainrater.service.RatingService
import com.kotlinswe.waterfountainrater.service.SearchService
import com.kotlinswe.waterfountainrater.util.DistanceCalculator
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.*
import kotlinx.coroutines.runBlocking

@Component
class CliRunner(
    private val buildingRepo: BuildingRepository,
    private val stationRepo: WaterStationRepository,
    private val fountainRepo: WaterFountainRepository,
    private val ratingService: RatingService,
    private val searchService: SearchService
) : CommandLineRunner {

    private val scanner = Scanner(System.`in`)

    override fun run(vararg args: String?) {
        println("\n🚰 Water Fountain Rater CLI")
        println("Type 'help' for commands, 'exit' to quit\n")

        // Running the CLI within a coroutine context
        runBlocking {
            while (true) {
                try {
                    print("> ")
                    val input = scanner.nextLine().trim()
                    val (command, args) = parseInput(input)

                    when (command.lowercase()) {
                        "exit" -> break
                        "help" -> printHelp()
                        "list" -> listBuildings()
                        "stations" -> listStations(args)
                        "fountains" -> listFountains(args)
                        "rate" -> rateFountain(args)
                        "top" -> showTopRated(args)
                        "near" -> findNearby(args)
                        "details" -> showDetails(args)
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
        println("""  
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
        """.trimIndent())
    }

    private fun listBuildings() {
        val buildings = buildingRepo.findAll()
        if (buildings.isEmpty()) {
            println("No buildings found.")
            return
        }

        println("\n🏢 Buildings:")
        buildings.forEach { building ->
            val stationCount = stationRepo.findByBuildingId(building.id).size
            println("  ${building.id} | ${building.name.padEnd(20)} | 📍 (${building.latitude}, ${building.longitude}) | 🚰 $stationCount stations")
        }
    }

    private fun listStations(args: String) {
        if (args.isBlank()) {
            println("Usage: stations [buildingId]")
            return
        }

        val stations = stationRepo.findByBuildingId(args.toLong())
        if (stations.isEmpty()) {
            println("No stations found for building $args")
            return
        }

        println("\n🚰 Water Stations:")
        stations.forEach { station ->
            val fountainCount = fountainRepo.findByStationId(station.id).size
            println("  ${station.id} | Floor ${station.floor} | ${station.description.padEnd(30)} | ⛲ $fountainCount fountains")
        }
    }

    private fun listFountains(args: String) {
        if (args.isBlank()) {
            println("Usage: fountains [stationId]")
            return
        }

        val fountains = fountainRepo.findByStationId(args.toLong())
        if (fountains.isEmpty()) {
            println("No fountains found for station $args")
            return
        }

        println("\n⛲ Fountains:")
        fountains.forEach { fountain ->
            println("  ${fountain.id} | ${fountain.type.toString().padEnd(12)} | ★ ${"%.2f".format(fountain.overallRating)} " +
                    "(T:${fountain.tasteRating} F:${fountain.flowRating} " +
                    "Temp:${fountain.temperatureRating} A:${fountain.ambienceRating} U:${fountain.usabilityRating})")
        }
    }

    private suspend fun rateFountain(args: String) {
        val parts = args.split("\\s+".toRegex())
        if (parts.size < 6) {
            println("Usage: rate [id] [taste] [flow] [temp] [amb] [usability]")
            return
        }

        try {
            val fountainId = parts[0].toLong()
            val fountain = ratingService.rateFountain(
                fountainId = fountainId,
                taste = parts[1].toDouble(),
                flow = parts[2].toDouble(),
                temperature = parts[3].toDouble(),
                ambience = parts[4].toDouble(),
                usability = parts[5].toDouble()
            )
            println("\n✅ Rated fountain ${fountain.id}")
            println("   New overall rating: ★ ${"%.2f".format(fountain.overallRating)}")
        } catch (e: Exception) {
            println("❌ Error rating fountain: ${e.message}")
        }
    }

    private suspend fun showTopRated(args: String) {
        val limit = args.toIntOrNull() ?: 5

        val fountains = ratingService.getTopRatedFountains(limit)
        if (fountains.isEmpty()) {
            println("No fountains found.")
            return
        }

        println("\n🏆 Top $limit Fountains:")
        fountains.forEachIndexed { index, fountain ->
            val station = stationRepo.findById(fountain.station.id).orElseThrow()
            val building = buildingRepo.findById(station.building.id).orElseThrow()

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
            val buildings = searchService.findBuildingsNear(
                latitude = parts[0].toDouble(),
                longitude = parts[1].toDouble(),
                radius = 100.0 // meters
            )

            if (buildings.isEmpty()) {
                println("No buildings found within 100m.")
                return
            }

            println("\n📍 Nearby Buildings (within 100m):")
            buildings.forEach { building ->
                val distance = DistanceCalculator.calculate(
                    parts[0].toDouble(),
                    parts[1].toDouble(),
                    building.latitude,
                    building.longitude
                )
                println("  🏢 ${building.name} (${"%.2f".format(distance)}m away) - ID: ${building.id}")
            }
        } catch (e: Exception) {
            println("❌ Error finding nearby buildings: ${e.message}")
        }
    }

    private fun showDetails(args: String) {
        val fountainId = args.toLongOrNull() ?: run {
            println("Invalid fountain ID.")
            return
        }

        val fountain = fountainRepo.findById(fountainId).orElse(null)
        if (fountain == null) {
            println("Fountain not found.")
            return
        }

        val station = stationRepo.findById(fountain.station.id).orElseThrow()
        val building = buildingRepo.findById(station.building.id).orElseThrow()

        println("\n⛲ Fountain Details:")
        println("  ID: ${fountain.id}")
        println("  Type: ${fountain.type}")
        println("  Location: ${building.name}, Floor ${station.floor}")
        println("  Station: ${station.description}")
        println("\n⭐ Ratings:")
        println("  Overall: ★ ${"%.2f".format(fountain.overallRating)}")
        println("  Taste: ${fountain.tasteRating}")
        println("  Flow: ${fountain.flowRating}")
        println("  Temperature: ${fountain.temperatureRating}")
        println("  Ambience: ${fountain.ambienceRating}")
        println("  Usability: ${fountain.usabilityRating}")
    }
}