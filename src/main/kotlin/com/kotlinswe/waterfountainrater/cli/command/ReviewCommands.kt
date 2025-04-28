package com.kotlinswe.waterfountainrater.cli.command

import com.kotlinswe.waterfountainrater.service.ReviewService
import com.kotlinswe.waterfountainrater.cli.util.listEntities
import com.kotlinswe.waterfountainrater.cli.formatter.formatAll
import com.kotlinswe.waterfountainrater.cli.formatter.formatById
import kotlinx.coroutines.coroutineScope

class ReviewCommands(
    private val reviewService: ReviewService
) {
    suspend fun listReviews(args: String) = coroutineScope {
        listEntities(
            args = args,
            listAll = { reviewService.getAllReviews() },
            findById = { fountainId -> reviewService.getReviewsByWaterFountain(fountainId) },
            formatAll = { review -> formatAll(review) },
            formatById = { reviewId -> formatById(reviewId) },
            usageMessage = "Usage: reviews [fountainId]"
        )
    }
}