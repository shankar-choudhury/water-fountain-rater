package com.kotlinswe.waterfountainrater.cli.formatter

import com.kotlinswe.waterfountainrater.model.WaterFountainReview

fun formatAll(review: WaterFountainReview): String {
    return """
        ★ ${"%.1f".format(review.averageRating)} - Fountain ${review.waterFountain.id}
           Ratings:
             Taste: ${"★".repeat(review.tasteRating.toInt())}${if (review.tasteRating % 1 >= 0.5) "½" else ""} (${"%.1f".format(review.tasteRating)})
             Flow: ${"★".repeat(review.flowRating.toInt())}${if (review.flowRating % 1 >= 0.5) "½" else ""} (${"%.1f".format(review.flowRating)})
             Temperature: ${"★".repeat(review.temperatureRating.toInt())}${if (review.temperatureRating % 1 >= 0.5) "½" else ""} (${"%.1f".format(review.temperatureRating)})
             Ambience: ${"★".repeat(review.ambienceRating.toInt())}${if (review.ambienceRating % 1 >= 0.5) "½" else ""} (${"%.1f".format(review.ambienceRating)})
             Usability: ${"★".repeat(review.usabilityRating.toInt())}${if (review.usabilityRating % 1 >= 0.5) "½" else ""} (${"%.1f".format(review.usabilityRating)})
           Review: ${review.review.take(50)}${if (review.review.length > 50) "..." else ""}
           By: Anonymous (ID: ${review.id}) | Created: ${review.createdAt}
    """.trimIndent()
}

fun formatById(review: WaterFountainReview): String {
    return """
        ★ ${"%.1f".format(review.averageRating)} (${review.waterFountain.id})
           Breakdown:
           🍽️ Taste: ${"%.1f".format(review.tasteRating)} | 💦 Flow: ${"%.1f".format(review.flowRating)}
           🌡️ Temp: ${"%.1f".format(review.temperatureRating)} | 🏙️ Ambience: ${"%.1f".format(review.ambienceRating)}
           ♿ Usability: ${"%.1f".format(review.usabilityRating)}
           Review: ${review.review.take(80)}${if (review.review.length > 80) "..." else ""}
           Submitted: ${review.createdAt} (ID: ${review.id})
    """.trimIndent()
}