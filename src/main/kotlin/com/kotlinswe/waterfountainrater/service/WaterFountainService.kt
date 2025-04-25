package com.kotlinswe.waterfountainrater.service

import com.kotlinswe.waterfountainrater.dto.waterfountain.WaterFountainDto
import com.kotlinswe.waterfountainrater.model.WaterFountain
import com.kotlinswe.waterfountainrater.repository.WaterFountainRepository
import org.springframework.stereotype.Service

@Service
class WaterFountainService(
    private val fountainRepository: WaterFountainRepository
) {
    suspend fun showDetails(fountainId: Long): WaterFountainDto =
        fountainRepository.findById(fountainId)
            .map { WaterFountainDto.from(it) }
            .orElse(
                WaterFountainDto(
                    id = 0,
                    type = WaterFountain.FountainType.NONE,
                    status = WaterFountain.FountainStatus.NONE,
                    overallRating = -1.0))

    suspend fun listFountains() =
        fountainRepository.findAll()
            .takeIf { it.isNotEmpty() }
            ?.map { WaterFountainDto.from(it) }
            ?: emptyList()

}