package com.kotlinswe.waterfountainrater.service

import com.kotlinswe.waterfountainrater.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.kotlinswe.waterfountainrater.model.WaterFountain
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class RatingService(
    private val fountainRepository: WaterFountainRepository
) {
    @Transactional
    suspend fun rateFountain(
        fountainId: Long,
        taste: Double,
        flow: Double,
        temperature: Double,
        ambience: Double,
        usability: Double
    ): WaterFountain = withContext(Dispatchers.IO) {
        val fountain = fountainRepository.findById(fountainId)
            .orElseThrow { IllegalArgumentException("Fountain not found") }

        fountain.tasteRating = taste
        fountain.flowRating = flow
        fountain.temperatureRating = temperature
        fountain.ambienceRating = ambience
        fountain.usabilityRating = usability

        fountainRepository.save(fountain)
    }

    suspend fun getTopRatedFountains(limit: Int = 10): List<WaterFountain> = withContext(Dispatchers.IO) {
        fountainRepository.findAll()
            .sortedByDescending { it.overallRating }
            .take(limit)
    }
}
