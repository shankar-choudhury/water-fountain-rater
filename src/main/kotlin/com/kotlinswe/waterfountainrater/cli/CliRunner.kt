package com.kotlinswe.waterfountainrater.cli

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