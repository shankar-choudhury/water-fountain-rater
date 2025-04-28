package com.kotlinswe.waterfountainrater.cli.command

import com.kotlinswe.waterfountainrater.service.ReviewService
import kotlinx.coroutines.coroutineScope

class StatsCommands(private val reviewService: ReviewService) {
    suspend fun showStats() = coroutineScope {
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

    suspend fun showFountainStats(args: String) = coroutineScope {
        val fountainId = args.toLongOrNull() ?: run {
            println("Usage: fountainstats [fountainId]")
            return@coroutineScope
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
}