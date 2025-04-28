package com.kotlinswe.waterfountainrater.cli.command

import com.kotlinswe.waterfountainrater.dto.review.WaterFountainReviewDto
import com.kotlinswe.waterfountainrater.service.ReviewService
import com.kotlinswe.waterfountainrater.service.WaterFountainService
import com.kotlinswe.waterfountainrater.cli.util.listEntities
import com.kotlinswe.waterfountainrater.cli.formatter.formatAll
import com.kotlinswe.waterfountainrater.cli.formatter.formatById
import com.kotlinswe.waterfountainrater.cli.formatter.formatDetails
import com.kotlinswe.waterfountainrater.cli.formatter.formatTopRated
import kotlinx.coroutines.coroutineScope
import java.util.*

class FountainCommands(
    private val fountainService: WaterFountainService,
    private val reviewService: ReviewService
) {
    suspend fun listFountains(args: String) = coroutineScope {
        listEntities(
            args = args,
            listAll = { fountainService.listAll() },
            findById = { stationId -> fountainService.findByStationId(stationId) },
            formatAll = { fountain -> formatAll(fountain) },
            formatById = { fountainId -> formatById(fountainId) },
            usageMessage = "Usage: fountains [stationId]"
        )
    }

    suspend fun showFountainDetails(args: String) = coroutineScope {
        val fountainId = args.toLongOrNull() ?: run {
            println("Usage: details [fountainId]")
            return@coroutineScope
        }
        val fountain = fountainService.showDetails(fountainId)
        println(formatDetails(fountain))
    }

    suspend fun rateFountain(args: String, scanner: Scanner) = coroutineScope {
        val parts = args.split("\\s+".toRegex())
        if (parts.size < 6) {
            println("Usage: rate [id] [taste] [flow] [temp] [amb] [usability]")
            return@coroutineScope
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

    suspend fun showTopRated(args: String) = coroutineScope {
        val limit = args.toIntOrNull() ?: 5
        val fountains = reviewService.getTopRatedFountains(limit)
        if (fountains.isEmpty()) {
            println("No fountains found.")
            return@coroutineScope
        }

        println("\n🏆 Top $limit Fountains:")
        fountains.forEachIndexed { index, fountain ->
            println(formatTopRated(index, fountain))
        }
    }
}