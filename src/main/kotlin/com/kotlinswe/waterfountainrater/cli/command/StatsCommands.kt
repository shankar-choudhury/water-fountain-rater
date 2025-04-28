package com.kotlinswe.waterfountainrater.cli.command

import com.kotlinswe.waterfountainrater.service.ReviewService
import kotlinx.coroutines.coroutineScope

class StatsCommands(private val reviewService: ReviewService) {
    suspend fun showStats(args: String) = coroutineScope {
        if (args.isBlank()) {
            showGlobalStats()
        } else {
            showFountainStats(args)
        }
    }

    private suspend fun showGlobalStats() = coroutineScope {
        val stats = reviewService.getStats()
        println(
            """
            📊 System Stats:
               Total Fountains: ${stats["totalFountains"]}
               Total Reviews: ${stats["totalReviews"]}
               Broken Fountains: ${stats["brokenFountains"]}
               Fountains With Broken Reports: ${stats["fountainsWithBrokenReports"]}
               Avg Reviews per Fountain: ${"%.2f".format(stats["avgReviewsPerFountain"] ?: 0.0)}
               Fountains Without Reviews: ${stats["fountainsWithoutReviews"]}
            
            🌟 Top Rated Fountain:
               ${stats["topRatedFountain"] ?: "None"}
    
            💀 Worst Rated Fountain:
               ${stats["worstRatedFountain"] ?: "None"}
            """.trimIndent()
        )
    }

    private suspend fun showFountainStats(fountainId: String) = coroutineScope {
        val id = fountainId.toLongOrNull() ?: run {
            println("❌ Invalid fountain ID. Usage: stats [fountainId]")
            return@coroutineScope
        }

        val stats = reviewService.getFountainRatingStats(id)
        println(
            """
            📊 Stats for Fountain $id:
               Total Reviews: ${stats["totalReviews"]}
               Average Rating: ${"%.2f".format(stats["averageRating"])}
               Total Reports: ${stats["reports"]}
            """.trimIndent()
        )
    }
}