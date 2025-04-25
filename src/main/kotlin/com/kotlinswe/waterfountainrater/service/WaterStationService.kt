package com.kotlinswe.waterfountainrater.service

import com.kotlinswe.waterfountainrater.dto.waterstation.WaterStationDto
import com.kotlinswe.waterfountainrater.repository.WaterFountainRepository
import com.kotlinswe.waterfountainrater.repository.WaterStationRepository
import org.springframework.stereotype.Service

@Service
class WaterStationService(
    private val stationRepository: WaterStationRepository,
    private val fountainRepository: WaterFountainRepository
) {
    suspend fun showDetails(stationId: Long) =
        stationRepository.findById(stationId).orElse(null)?.let { station ->
            WaterStationDto.from(station)
        } ?: WaterStationDto(
            id = 0,
            floor = 0,
            description = "Station not found",
            fountains = emptyList()
        )

    suspend fun listStations() =
        stationRepository.findAll().takeIf { it.isNotEmpty() }
            ?.map { WaterStationDto.from(it) }
            ?: emptyList()
}