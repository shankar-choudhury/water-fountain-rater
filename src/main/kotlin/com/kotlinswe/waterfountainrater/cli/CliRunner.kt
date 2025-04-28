package com.kotlinswe.waterfountainrater.cli

import com.kotlinswe.waterfountainrater.dto.review.WaterFountainReviewDto
import com.kotlinswe.waterfountainrater.dto.waterfountain.WaterFountainDto
import com.kotlinswe.waterfountainrater.dto.waterstation.WaterStationDto
import com.kotlinswe.waterfountainrater.model.WaterFountain
import com.kotlinswe.waterfountainrater.service.*
import com.kotlinswe.waterfountainrater.util.DistanceCalculator
import jakarta.transaction.Transactional
import kotlinx.coroutines.*
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.*
import kotlin.coroutines.CoroutineContext

@Component
class CliRunner(
    private val buildingService: BuildingService,
    private val stationService: WaterStationService,
    private val fountainService: WaterFountainService,
    private val reviewService: ReviewService,
    private val reportService: ReportService,
    private val searchService: SearchService
) : CommandLineRunner, CoroutineScope {

    // Create a dedicated scope for CLI operations
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Default + job

    private val scanner = Scanner(System.`in`)

    override fun run(vararg args: String?) {
        println("\n🚰 Water Fountain Rater CLI")
        println("Type 'help' for commands, 'exit' to quit\n")

        // Use launch instead of runBlocking
        launch {
            try {
                while (isActive) {  // Check if scope is still active
                    try {
                        print("> ")
                        val input = scanner.nextLine().trim()
                        if (input == "exit") break

                        val (command, args) = parseInput(input)
                        handleCommand(command, args)
                    } catch (e: Exception) {
                        println("⚠️ Error: ${e.message}")
                    }
                }
            } finally {
                shutdown()
            }
        }.invokeOnCompletion { cause ->
            cause?.let { println("CLI terminated due to: ${it.message}") }
        }
    }

    private suspend fun handleCommand(command: String, args: String) {
        when (command.lowercase()) {
            "help" -> printHelp()
            "buildings" -> listBuildings(args)
            "stations" -> listStations(args)
            "fountains" -> listFountains(args)
            "rate" -> rateFountain(args)
            "top" -> showTopRated(args)
            "near" -> findNearby(args)
            "details" -> showFountainDetails(args)
            "report" -> submitReport(args)
            "search" -> searchFountains(args)
            "stats" -> showStats()
            "reviews" -> showReviews(args)
            "all reports" -> listAllReports()
            "fountain reports" -> listFountainReports(args)
            "fountain stats" -> showFountainStats(args)
            "building stations" -> listBuildingStations(args)
            "nearby with fountains" -> findNearbyWithFountains(args)
            else -> println("❌ Unknown command. Type 'help' for options.")
        }
    }

    fun shutdown() {
        job.cancel("CLI shutdown")
        scanner.close()
        println("\n👋 Goodbye!")
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
          nearbywithfountains [lat] [lon] - Find buildings with fountains near location

        STATIONS:
          stations [buildingId] - List stations in a building
          buildingstations [buildingId] - Alternative stations listing

        FOUNTAINS:
          fountains [stationId] - List fountains in a station
          rate [id] [ratings...] - Rate a fountain
          top [limit]        - Show top-rated fountains (default: 5)
          details [id]       - Show fountain details
          reviews [id]      - Show fountain reviews
          search [query]     - Search fountains by type/description
          report [id] [issue]- Report a fountain issue
          fountainstats [id] - Show fountain statistics
          fountainreports [id] - Show reports for a fountain

        REPORTS:
          allreports        - List all system reports

        SYSTEM:
          stats             - Show system statistics
          help              - Show this help
          exit              - Exit the application
        """.trimIndent()
        )
    }

    private suspend fun listBuildings(args: String) {
        buildingService.listAll().forEach { println("🏢 ${it.name} (ID: ${it.id})") }
    }

    private suspend fun listStations(args: String) {
        listEntities(
            args = args,
            listAll = { stationService.listAll() },
            findById = { buildingId -> stationService.findByBuildingId(buildingId) },
            formatAll = ::formatAllStations,
            formatById = ::formatStationById,
            usageMessage = "Usage: stations [buildingId]  (e.g. 'stations 1' for Building 1)"
        )
    }

    private suspend fun listFountains(args: String) {
        listEntities(
            args = args,
            listAll = { fountainService.listAll() },
            findById = { stationId -> fountainService.findByStationId(stationId) },
            formatAll = ::formatAllFountains,
            formatById = ::formatFountainById,
            usageMessage = "Usage: fountains [stationId]  (e.g. 'fountains 1' for Station 1)"
        )
    }

    private suspend fun <T> listEntities(
        args: String,
        listAll: suspend () -> List<T>,
        findById: suspend (Long) -> List<T>,
        formatAll: (T) -> String,
        formatById: (T) -> String,
        usageMessage: String
    ) {
        if (args.isBlank()) {
            listAll().forEach { entity ->
                println(formatAll(entity))
            }
        } else {
            val id = args.toLongOrNull() ?: run {
                println(usageMessage)
                return
            }
            findById(id).forEach { entity ->
                println(formatById(entity))
            }
        }
    }

    // Station formatting
    private fun formatAllStations(station: WaterStationDto): String {
        return "🚰 Station ${station.id}: Building ${station.buildingId} Floor ${station.floor}, ${station.description}"
    }

    private fun formatStationById(station: WaterStationDto): String {
        return "🚰 Station ${station.id}: Floor ${station.floor}, ${station.description}"
    }

    // Fountain formatting
    private fun formatAllFountains(fountain: WaterFountainDto): String {
        return "💧 Fountain ${fountain.id}: Station ${fountain.stationId} Type ${fountain.type}, ★ ${"%.1f".format(fountain.overallRating)}"
    }

    private fun formatFountainById(fountain: WaterFountainDto): String {
        return "💧 Fountain ${fountain.id}: Type ${fountain.type}, ★ ${"%.1f".format(fountain.overallRating)}"
    }

    private suspend fun showFountainDetails(args: String) {
        val fountainId = args.toLongOrNull() ?: run {
            println("Usage: details [fountainId]")
            return
        }
        val fountain = fountainService.showDetails(fountainId)
        println(
            """
            💧 Fountain ${fountain.id} Details:
               Type: ${fountain.type}
               Status: ${fountain.status}
               Overall Rating: ★ ${"%.2f".format(fountain.overallRating)}
               Station: ${fountain.stationId}
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
                review = reviewText
            )

            val review = reviewService.addReview(reviewDto)
            println("\n✅ Submitted review for fountain ${review.waterFountain.id}")
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
        fountains.forEachIndexed { index, fountain ->
            println("  ${index + 1}. ★ ${"%.2f".format(fountain.overallRating)} - ${fountain.type}")
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

            val buildings = searchService.findBuildingsNear(latitude, longitude, radius * 1.1)
                .filter { building ->
                    DistanceCalculator.calculate(latitude, longitude, building.latitude, building.longitude) <= radius
                }

            if (buildings.isEmpty()) {
                println("No buildings found within $radius m.")
                return
            }

            println("\n📍 Nearby Buildings (within $radius m):")
            buildings.forEach { building ->
                val distance = DistanceCalculator.calculate(latitude, longitude, building.latitude, building.longitude)
                println("  🏢 ${building.name} (${"%.2f".format(distance)}m away) - ID: ${building.id}")
            }
        } catch (e: Exception) {
            println("❌ Error finding nearby buildings: ${e.message}")
        }
    }

    private suspend fun submitReport(args: String) {
        val parts = args.split(" ")
        if (parts.size < 2) {
            println("Usage: report [fountainId] [issue]")
            return
        }

        val fountainId = parts[0].toLongOrNull() ?: run {
            println("❌ Invalid fountain ID")
            return
        }

        val issue = parts[1].lowercase()
        val status = when (issue) {
            "broken" -> WaterFountain.FountainStatus.BROKEN
            "leaking" -> WaterFountain.FountainStatus.OUT_OF_ORDER
            "dirty" -> WaterFountain.FountainStatus.UNDER_REPAIR
            else -> WaterFountain.FountainStatus.NONE
        }

        print("Enter report details: ")
        val reportContents = scanner.nextLine()

        try {
            val report = reportService.submitReport(fountainId, status, reportContents)
            println("✅ Reported issue for fountain $fountainId (Status: ${report.status})")
        } catch (e: Exception) {
            println("❌ Failed to submit report: ${e.message}")
        }
    }

    private suspend fun searchFountains(args: String) {
        if (args.isBlank()) {
            println("Usage: search [query]")
            return
        }

        val results = searchService.searchFountains(args)
        if (results.isEmpty()) {
            println("🔍 No fountains matching '$args'")
            return
        }

        println("\n🔍 Search Results:")
        results.forEach { fountain ->
            println("  ${fountain.id}. ${fountain.type} (★ ${"%.1f".format(fountain.overallRating)})")
            println("     Status: ${fountain.status}")
        }
    }

    private suspend fun showStats() {
        val stats = reviewService.getStats()
        println(
            """
            📊 System Stats:
               Total Fountains: ${stats["totalFountains"]}
               Avg Rating: ${"%.2f".format(stats["avgRating"])}
               Broken Fountains: ${stats["brokenFountains"]}
            """.trimIndent()
        )
    }

    private suspend fun showReviews(args: String) {
        val fountainId = args.toLongOrNull() ?: run {
            println("Usage: reviews [fountainId]")
            return
        }

        val reviews = reviewService.getReviews(fountainId)
        if (reviews.isEmpty()) {
            println("No reviews for fountain $fountainId")
            return
        }

        println("\n📝 Reviews for Fountain $fountainId:")
        reviews.forEach { review ->
            println("  ★ ${"%.1f".format(review.averageRating)} - ${review.review.take(50)}...")
        }
    }

    private suspend fun listAllReports() {
        val reports = reportService.getAllReports()
        if (reports.isEmpty()) {
            println("No reports found in system")
            return
        }

        println("\n📝 All Reports:")
        reports.forEach { report ->
            println("  ⚠️ Report ${report.id} for Fountain ${report.fountainId}")
            println("     Status: ${report.status}")
            println("     Details: ${report.reportContents.take(50)}...")
        }
    }

    private suspend fun listFountainReports(args: String) {
        val fountainId = args.toLongOrNull() ?: run {
            println("Usage: fountainreports [fountainId]")
            return
        }

        val reports = reportService.getReportsForFountain(fountainId)
        if (reports.isEmpty()) {
            println("No reports found for fountain $fountainId")
            return
        }

        println("\n📝 Reports for Fountain $fountainId:")
        reports.forEach { report ->
            println("  ⚠️ Report ${report.id}")
            println("     Status: ${report.status}")
            println("     Details: ${report.reportContents.take(50)}...")
        }
    }

    private suspend fun showFountainStats(args: String) {
        val fountainId = args.toLongOrNull() ?: run {
            println("Usage: fountainstats [fountainId]")
            return
        }

        val stats = reviewService.getFountainRatingStats(fountainId)
        println(
            """
        📊 Stats for Fountain $fountainId:
           Total Reviews: ${stats["totalReviews"]}
           Average Rating: ${"%.2f".format(stats["averageRating"])}
           Total Reports: ${stats["reports"]}
        """.trimIndent()
        )
    }

    private suspend fun listBuildingStations(args: String) {
        val buildingId = args.toLongOrNull() ?: run {
            println("Usage: buildingstations [buildingId]")
            return
        }

        val stations = searchService.getStationsForBuilding(buildingId)
        if (stations.isEmpty()) {
            println("No stations found for building $buildingId")
            return
        }

        println("\n🚰 Stations in Building $buildingId:")
        stations.forEach { station ->
            println("  Station ${station.id}: Floor ${station.floor}, ${station.description}")
        }
    }

    private suspend fun findNearbyWithFountains(args: String) {
        val parts = args.split("\\s+".toRegex())
        if (parts.size < 2) {
            println("Usage: nearbywithfountains [latitude] [longitude]")
            return
        }

        try {
            val latitude = parts[0].toDouble()
            val longitude = parts[1].toDouble()
            val radius = 100.0

            val buildings = searchService.findNearbyBuildingsWithFountains(latitude, longitude, radius)
            if (buildings.isEmpty()) {
                println("No buildings with fountains found within $radius m.")
                return
            }

            println("\n📍 Nearby Buildings With Fountains (within $radius m):")
            buildings.forEach { building ->
                val distance = DistanceCalculator.calculate(latitude, longitude, building.latitude, building.longitude)
                println("  🏢 ${building.name} (${"%.2f".format(distance)}m away)")
                println("     Stations: ${building.waterStations.size}")
            }
        } catch (e: Exception) {
            println("❌ Error finding nearby buildings: ${e.message}")
        }
    }
}