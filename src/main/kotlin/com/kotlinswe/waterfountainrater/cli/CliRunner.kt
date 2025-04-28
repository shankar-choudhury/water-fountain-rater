package com.kotlinswe.waterfountainrater.cli

/*import com.kotlinswe.waterfountainrater.dto.building.BuildingSearchResponseDto
import com.kotlinswe.waterfountainrater.dto.report.ReportResponseDto
import com.kotlinswe.waterfountainrater.dto.review.WaterFountainReviewDto
import com.kotlinswe.waterfountainrater.dto.waterfountain.WaterFountainDto
import com.kotlinswe.waterfountainrater.dto.waterstation.WaterStationDto
import com.kotlinswe.waterfountainrater.model.WaterFountain
import com.kotlinswe.waterfountainrater.model.WaterFountainReview
import com.kotlinswe.waterfountainrater.service.*
import com.kotlinswe.waterfountainrater.util.DistanceCalculator
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
            "reviews" -> listReviews(args)
            "reports" -> listReports(args)
            "fountain stats" -> showFountainStats(args)
            "nearby with fountains" -> findNearbyWithFountains(args)
            "quit" -> shutdown()
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
          buildings          - List all buildings
          near [lat] [lon]   - Find buildings near location
          nearbywithfountains [lat] [lon] - Find buildings with fountains nearby

        STATIONS:
          stations           - List all stations
          stations [buildingId] - List stations in a specific building

        FOUNTAINS:
          fountains          - List all fountains
          fountains [stationId] - List fountains in a specific station
          details [id]       - Show fountain details
          rate [id] [taste] [flow] [temp] [amb] [usability] - Rate a fountain
          top [limit]        - Show top-rated fountains (default: 5)
          search [query]     - Search fountains by type/description

        REVIEWS:
          reviews            - List all reviews
          reviews [fountainId] - List reviews for a specific fountain

        REPORTS:
          reports            - List all reports
          reports [fountainId] - List reports for a specific fountain
          report [id] [issue] - Report a fountain issue (broken/leaking/dirty)

        STATISTICS:
          stats              - Show system statistics
          fountainstats [id] - Show statistics for a specific fountain

        LOCATION:
          near [lat] [lon]   - Find buildings near location
          nearbywithfountains [lat] [lon] - Find buildings with fountains nearby

        SYSTEM:
          help               - Show this help
          exit               - Exit the application
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

    private suspend fun listReports(args: String) {
        listEntities(
            args = args,
            listAll = { reportService.getAllReports() },
            findById = { fountainId -> reportService.getReportsForFountain(fountainId) },
            formatAll = ::formatAllReports,
            formatById = ::formatFountainReports,
            usageMessage = "Usage: reports [fountainId]"
        )
    }

    private suspend fun listReviews(args: String) {
        listEntities(
            args = args,
            listAll = { reviewService.getAllReviews() }, // You'll need to add this method to ReviewService
            findById = { fountainId -> reviewService.getReviewsByWaterFountain(fountainId) },
            formatAll = ::formatAllReviews,
            formatById = ::formatFountainReviews,
            usageMessage = "Usage: reviews [fountainId]"
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

    // Report formatting
    private fun formatAllReports(report: ReportResponseDto): String {
        return """
        ⚠️ Report ${report.id} for Fountain ${report.fountainId}
           Status: ${report.status}
           Details: ${report.reportContents.take(50)}...
    """.trimIndent()
    }

    private fun formatFountainReports(report: ReportResponseDto): String {
        return """
        ⚠️ Report ${report.id}
           Status: ${report.status}
           Details: ${report.reportContents.take(50)}...
    """.trimIndent()
    }

    // Add these formatting functions
    private fun formatAllReviews(review: WaterFountainReview): String {
        return """
        ★ ${"%.1f".format(review.averageRating)} - Fountain ${review.waterFountain.id}
           Review: ${review.review.take(50)}...
           By: Anonymous (ID: ${review.id})
    """.trimIndent()
    }

    private fun formatFountainReviews(review: WaterFountainReview): String {
        return """
        ★ ${"%.1f".format(review.averageRating)} 
           Review: ${review.review.take(50)}...
           By: Anonymous (ID: ${review.id})
    """.trimIndent()
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

        val status = when (parts[1].lowercase()) {
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

    private suspend fun <T> searchEntities(
        query: String,
        searchFunction: suspend (String) -> List<T>,
        formatResult: (T) -> String,
        usageMessage: String,
        emptyMessage: (String) -> String
    ) {
        if (query.isBlank()) {
            println(usageMessage)
            return
        }

        val results = searchFunction(query)
        if (results.isEmpty()) {
            println(emptyMessage(query))
            return
        }

        println("\n🔍 Search Results:")
        results.forEach { entity ->
            println(formatResult(entity))
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

    private suspend fun findNearby(args: String) {
        findNearbyBuildings(
            args = args,
            fetchBuildings = { lat, lon, radius -> searchService.findBuildingsNear(lat, lon, radius * 1.1) },
            title = "Nearby Buildings"
        )
    }

    private suspend fun findNearbyWithFountains(args: String) {
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
    ) {
        val parts = args.split("\\s+".toRegex())
        if (parts.size < 2) {
            println("Usage: [latitude] [longitude]")
            return
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
                return
            }

            println("\n📍 $title (within $radius m):")
            buildings.forEach { building ->
                val distance = DistanceCalculator.calculate(latitude, longitude, building.latitude, building.longitude)
                println("  🏢 ${building.name} (${"%.2f".format(distance)}m away) - ID: ${building.id}")
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
*/

import com.kotlinswe.waterfountainrater.cli.command.*
import com.kotlinswe.waterfountainrater.cli.util.parseInput
import kotlinx.coroutines.*
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.*
import kotlin.coroutines.CoroutineContext

@Component
class CliRunner(
    private val buildingCommands: BuildingCommands,
    private val stationCommands: StationCommands,
    private val fountainCommands: FountainCommands,
    private val reviewCommands: ReviewCommands,
    private val reportCommands: ReportCommands,
    private val searchCommands: SearchCommands,
    private val statsCommands: StatsCommands
) : CommandLineRunner, CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Default + job
    private val scanner = Scanner(System.`in`)

    override fun run(vararg args: String?) {
        println("\n🚰 Water Fountain Rater CLI")
        println("Type 'help' for commands, 'exit' to quit\n")

        launch {
            try {
                while (isActive) {
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
            "buildings" -> buildingCommands.listBuildings(args)
            "stations" -> stationCommands.listStations(args)
            "fountains" -> fountainCommands.listFountains(args)
            "rate" -> fountainCommands.rateFountain(args, scanner)
            "top" -> fountainCommands.showTopRated(args)
            "near" -> searchCommands.findNearby(args)
            "details" -> fountainCommands.showFountainDetails(args)
            "report" -> reportCommands.submitReport(args, scanner)
            "search" -> searchCommands.searchFountains(args)
            "stats" -> statsCommands.showStats(args)
            "reviews" -> reviewCommands.listReviews(args)
            "reports" -> reportCommands.listReports(args)
            "nearby with fountains" -> searchCommands.findNearbyWithFountains(args)
            "quit" -> shutdown()
            else -> println("❌ Unknown command. Type 'help' for options.")
        }
    }

    fun shutdown() {
        job.cancel("CLI shutdown")
        scanner.close()
        println("\n👋 Goodbye!")
    }

    private fun printHelp() {
        println(
            """
            📋 Available Commands:
    
            BUILDINGS:
              buildings          - List all buildings
              near [lat] [lon]   - Find buildings near location
    
            STATIONS:
              stations           - List all stations
              stations [buildingId] - List stations in a specific building
    
            FOUNTAINS:
              fountains          - List all fountains
              fountains [stationId] - List fountains in a specific station
              details [id]       - Show fountain details
              rate [id] [taste] [flow] [temp] [amb] [usability] - Rate a fountain
              top [limit]        - Show top-rated fountains (default: 5)
              search [query]     - Search fountains by type/description
    
            REVIEWS:
              reviews            - List all reviews
              reviews [fountainId] - List reviews for a specific fountain
    
            REPORTS:
              reports            - List all reports
              reports [fountainId] - List reports for a specific fountain
              report [id] [issue] - Report a fountain issue (broken/leaking/dirty)
    
            STATISTICS:
              stats              - Show system statistics
              stats [id]        - Show statistics for a specific fountain
    
            LOCATION:
              near [lat] [lon]   - Find buildings near location
              nearbywithfountains [lat] [lon] - Find buildings with fountains nearby
    
            SYSTEM:
              help               - Show this help
              exit               - Exit the application
        """.trimIndent()
        )
    }
}