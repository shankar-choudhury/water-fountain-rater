package com.kotlinswe.waterfountainrater.cli.formatter

import com.kotlinswe.waterfountainrater.model.WaterFountainReview

fun formatAll(review: WaterFountainReview): String {
    return """
        ★ ${"%.1f".format(review.averageRating)} - Fountain ${review.waterFountain.id}
           Review: ${review.review.take(50)}...
           By: Anonymous (ID: ${review.id})
    """.trimIndent()
}

fun formatById(review: WaterFountainReview): String {
    return """
        ★ ${"%.1f".format(review.averageRating)} 
           Review: ${review.review.take(50)}...
           By: Anonymous (ID: ${review.id})
    """.trimIndent()
}